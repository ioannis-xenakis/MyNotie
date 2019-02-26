package com.example.fanatic_coder.mynotepad;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.fanatic_coder.mynotepad.adapters.NotesAdapter;
import com.example.fanatic_coder.mynotepad.db.NotesDAO;
import com.example.fanatic_coder.mynotepad.db.NotesDB;
import com.example.fanatic_coder.mynotepad.model.Note;

import java.util.ArrayList;
import java.util.Date;

public class MainNotesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Note> notes;
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
    }

    private void scrollToNewNote() {

        recyclerView.scrollToPosition(adapter.getItemCount());

    }

    private void onAddNewNote() {
        //Open MainActivity for start writing the new note
        startActivity(new Intent(this, MainActivity.class));

        /* Commented for future reference
        if (notes != null) {
            notes.add(new Note("This is new note.", new Date().getTime()));
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } */
    }

    private void loadNotes() {
        this.notes = new ArrayList<>();
        this.notes = dao.getNotes(); //Get all notes from database
        this.adapter = new NotesAdapter(notes, this);
        this.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume () {
        super.onResume();
        loadNotes();
    }

}
