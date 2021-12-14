package com.code_that_up.john_xenakis.my_notie;

import static com.code_that_up.john_xenakis.my_notie.utils.OtherUtils.closeKeyboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.code_that_up.john_xenakis.my_notie.db.NotesDAO;
import com.code_that_up.john_xenakis.my_notie.db.NotesDB;
import com.code_that_up.john_xenakis.my_notie.model.Note;
import com.code_that_up.john_xenakis.my_notie.utils.NoteUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.Objects;
/*
    My Notie is a note taking app, write notes and save them to see them and remember later.
    Copyright (C) 2021  Ioannis Xenakis

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

    Anything you want to contact me for, contact me with an e-mail, at: Xenakis.i.Contact@gmail.com
    I'll be happy to help you, or discuss anything with you! */

/**
 * <h2>EditNoteActivity</h2> is the Activity for editing a new/existing note.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see <a href="https://github.com/ioannis-xenakis/MyNotie">https://github.com/ioannis-xenakis/MyNotie</a> This project, is uploaded at Github. Visit it if you want!
 */
public class EditNoteActivity extends AppCompatActivity {

    private TextInputEditText noteTitle;
    private TextInputEditText noteBodyText;
    private Note note;
    private NotesDAO notesDAO;
    private String oldNoteTitle;
    private String oldNoteBodyText;
    private boolean isItNewNote;
    public static final String NOTE_EXTRA_KEY = "note_id";
    public static final String TAG = "MyNotie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        noteTitle = findViewById(R.id.note_title);
        noteBodyText = findViewById(R.id.note_body_text);
        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar_edit_note);

        notesDAO = NotesDB.getInstance(this).notesDAO();
        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_KEY, 0);
            note = notesDAO.getNoteById(id);
            noteTitle.setText(note.getNoteTitle());
            noteBodyText.setText(note.getNoteBodyText());
            isItNewNote = false;
        } else {
            isItNewNote = true;
            note = new Note();
            NoteUtils.increaseNoteIdByOne(notesDAO, note);
        }

        oldNoteTitle = Objects.requireNonNull(noteTitle.getText()).toString();
        oldNoteBodyText = Objects.requireNonNull(noteBodyText.getText()).toString();

        //Left arrow/Go back icon, on Top App Bar.
        topAppBar.setNavigationOnClickListener(view -> finish());
    }

    /**
     * saveNote, saves the note, the user has typed.
     */
    private void saveNote() {
        String noteTitleString = Objects.requireNonNull(noteTitle.getText()).toString();
        String noteBodyTextString = Objects.requireNonNull(noteBodyText.getText()).toString();

        long date = new Date().getTime();

        note.setNoteTitle(noteTitleString);
        note.setNoteBodyText(noteBodyTextString);
        note.setNoteDate(date);

        if (noteTitleString.equals("") && noteBodyTextString.equals("")) {
            notesDAO.deleteNote(note);
        } else if (oldNoteTitle.equals(noteTitleString) && oldNoteBodyText.equals(noteBodyTextString)) {
            finishNoSave();
        } else {
            saveNewNoteOrUpdateNote();
        }
    }

    private void saveNewNoteOrUpdateNote() {
        if (isItNewNote) {
            try {
                notesDAO.insertNote(note);
                Toast.makeText(getApplicationContext(), "New note saved!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Saving new note failed!", Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                notesDAO.updateNote(note);
                Toast.makeText(getApplicationContext(), "Note saved!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Saving note failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void finish() {
        saveNote();
        closeKeyboard(this);
        super.finish();
    }

    private void finishNoSave() {
        super.finish();
    }

    public void onDeleteNoteClick(MenuItem menuItem) {
        notesDAO.deleteNote(note);
        finishNoSave();
        Toast.makeText(getApplicationContext(), "Note deleted!", Toast.LENGTH_SHORT).show();
    }

    public void onRevertChangesClick(MenuItem menuItem) {
        noteTitle.setText(oldNoteTitle);
        noteBodyText.setText(oldNoteBodyText);
        Toast.makeText(getApplicationContext(), "Changes Reverted!", Toast.LENGTH_SHORT).show();
    }

    public void onAddToFoldersButtonClick(MenuItem menuItem) {
        if (note != null) {
            saveNewNoteOrUpdateNote();
            isItNewNote = false;
            Intent addToFolder = new Intent(this, AddToFoldersActivity.class);
            Log.d(TAG, "AddToFoldersButtonClick, note id: " + note.getId());
            addToFolder.putExtra(AddToFoldersActivity.NOTE_EXTRA_KEY, note.getId());
            startActivity(addToFolder);
        } else {
            Log.d(TAG, "Note is null(doesn't exist).");
            Toast.makeText(getApplicationContext(), "Adding to folders failed!", Toast.LENGTH_SHORT).show();
        }
    }
}