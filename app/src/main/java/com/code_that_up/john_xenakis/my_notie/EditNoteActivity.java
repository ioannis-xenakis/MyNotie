package com.code_that_up.john_xenakis.my_notie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.code_that_up.john_xenakis.my_notie.db.NotesDAO;
import com.code_that_up.john_xenakis.my_notie.db.NotesDB;
import com.code_that_up.john_xenakis.my_notie.model.Note;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.Objects;

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

        if(oldNoteTitle.equals(noteTitleString) && oldNoteBodyText.equals(noteBodyTextString)) {
            finishNoSave();
        }
        else {
            if (isItNewNote) {
                notesDAO.insertNote(note);
            }
            else {
                notesDAO.updateNote(note);
            }
        }
    }

    @Override
    public void finish() {
        saveNote();
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
}