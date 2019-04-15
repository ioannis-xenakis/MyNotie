package com.example.fanatic_coder.mynotepad.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fanatic_coder.mynotepad.R;
import com.example.fanatic_coder.mynotepad.callbacks.DeleteThisNoteListener;
import com.example.fanatic_coder.mynotepad.callbacks.NoteEventListener;
import com.example.fanatic_coder.mynotepad.model.Note;
import com.example.fanatic_coder.mynotepad.utils.NoteUtils;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder>{

    private ArrayList<Note> notes;
    private Context context;
    private NoteEventListener listener;
    private DeleteThisNoteListener deleteThisNoteListener;

    public NotesAdapter(ArrayList<Note> notes, Context context, DeleteThisNoteListener deleteThisNoteListener) {
        this.notes = notes;
        this.context = context;
        this.deleteThisNoteListener = deleteThisNoteListener;
    }

    //method onCreateViewHolder is called when a new view is created
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notes_layout, parent, false);
        return new NoteHolder(v);
    }

    //method onBindViewHolder displays data at specified position
    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        final Note note = getNote(position);
        if(note != null) {
            holder.noteTitle.setText(note.getWriteText());
            holder.noteDate.setText(NoteUtils.dateFromLong(note.getNoteDate()));

            //init note click event
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listener for when clicking on noteTitle and noteDate.
                    listener.onNoteClick(note);
                }
            });

            //init deleteOnlyNote button click event
            holder.deleteOnlyNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //deleteThisNoteListener object is a listener when clicking on deleteThisNote button and deletes the particular note.
                    deleteThisNoteListener.onDeleteThisNoteClick(note);
                }
            });
        }
    }

    //method getNote gets the notes position
    private Note getNote (int position) {
        return notes.get(position);
    }

    //method getItemCount gets how many notes will be created
    @Override
    public int getItemCount() {
        return notes.size();
    }

    //Holder class for the whole note
    class NoteHolder extends RecyclerView.ViewHolder{
        TextView noteTitle, noteDate;
        //The actual button to delete the note
        RelativeLayout deleteOnlyNote;

        NoteHolder(@NonNull View itemView) {
            super(itemView);
            //assigning note Date and finding View by id.
            noteDate = itemView.findViewById(R.id.noteDate);
            //assigning written note and finding View by id.
            noteTitle = itemView.findViewById(R.id.noteTitle);
            //assigning delete Only This Note button and finding View by id.
            deleteOnlyNote = itemView.findViewById(R.id.delete_only_this_note);
        }
    }

    //method to set all the Listeners
    public void setListener(NoteEventListener listener, DeleteThisNoteListener deleteThisNoteListener) {
        this.listener = listener;
        this.deleteThisNoteListener = deleteThisNoteListener;
    }

}
