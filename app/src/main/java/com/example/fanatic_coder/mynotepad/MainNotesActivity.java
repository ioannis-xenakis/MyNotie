package com.example.fanatic_coder.mynotepad;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.fanatic_coder.mynotepad.adapters.NotesAdapter;
import com.example.fanatic_coder.mynotepad.model.Note;

import java.util.ArrayList;
import java.util.Date;

public class MainNotesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Note> notes;
    private NotesAdapter adapter;

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
                onAddNewNote();
            }
        });
    }

    private void onAddNewNote() {
        if (notes != null) {
            notes.add(new Note("This is new note.", new Date().getTime()));
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void loadNotes() {
        this.notes = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            notes.add(new Note("This is a Notepad Demo. This is a Notepad Demo. This is a Notepad Demo. This is a Notepad Demo. This is a Notepad Demo...", new Date().getTime()));
        }
        adapter = new NotesAdapter(notes, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume () {
        super.onResume();
        loadNotes();
    }

}
