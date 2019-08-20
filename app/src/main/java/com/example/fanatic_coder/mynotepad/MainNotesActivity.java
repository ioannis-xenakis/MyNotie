package com.example.fanatic_coder.mynotepad;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.fanatic_coder.mynotepad.adapters.NotesAdapter;
import com.example.fanatic_coder.mynotepad.callbacks.DeleteThisNoteListener;
import com.example.fanatic_coder.mynotepad.callbacks.NoteEventListener;
import com.example.fanatic_coder.mynotepad.db.NotesDAO;
import com.example.fanatic_coder.mynotepad.db.NotesDB;
import com.example.fanatic_coder.mynotepad.model.Note;

import java.util.ArrayList;
import java.util.List;

import static com.example.fanatic_coder.mynotepad.MainActivity.NOTE_EXTRA_KEY;

public class MainNotesActivity extends AppCompatActivity implements NoteEventListener, DeleteThisNoteListener {

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private NotesDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_notes);

        //init recyclerView
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //init fab Button
        FloatingActionButton add_new_button = findViewById(R.id.add_new_note);
        add_new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToNewNote();
                onAddNewNote();
            }
        });
        dao = NotesDB.getInstance(this).notesDAO();

        FloatingActionButton delete_multi_note_button = findViewById(R.id.delete_note);
        delete_multi_note_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteMultiNotes();
            }
        });

        final CheckBox cbAllNotes = findViewById(R.id.cbAllNotes);
        cbAllNotes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (cbAllNotes.isChecked())adapter.selectAllNotes();
                else adapter.unselectAllNotes();
            }
        });
    }

    private void scrollToNewNote() {
        recyclerView.scrollToPosition(adapter.getItemCount());
    }

    private void onAddNewNote() {
        //Open MainActivity for start writing the new note
        startActivity(new Intent(this, MainActivity.class));
    }

    private void loadNotes() {
        List<Note> list = dao.getNotes(); //Get all notes from database
        ArrayList<Note> notes = new ArrayList<>(list);
        this.adapter = new NotesAdapter(notes, this, this);
        this.adapter.setListener(this, this); //set listener to adapter
        this.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    public void onNoteClick(Note note){
        // TODO: 20/03/2019 When note clicked, edit note
        Intent edit = new Intent(this, MainActivity.class);
        edit.putExtra(NOTE_EXTRA_KEY, note.getId());
        startActivity(edit);
    }

    @Override
    public void onDeleteThisNoteClick(Note note) {
        dao.deleteNote(note);
        loadNotes();
        Toast.makeText(this, "Note deleted!", Toast.LENGTH_SHORT).show();
    }

    public void onDeleteMultiNotes() {
        List<Note> checkedNotes = adapter.getCheckedNotes();
        if (checkedNotes.size() != 0) {
            for (Note note : checkedNotes) {
                dao.deleteNote(note);
            }

            loadNotes();
            Toast.makeText(this, checkedNotes.size()+" Note(s) deleted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No note selected.", Toast.LENGTH_SHORT).show();
        }
    }

}
