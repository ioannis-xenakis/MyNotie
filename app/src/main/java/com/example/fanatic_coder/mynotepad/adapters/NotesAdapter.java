package com.example.fanatic_coder.mynotepad.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fanatic_coder.mynotepad.R;
import com.example.fanatic_coder.mynotepad.callbacks.DeleteThisNoteListener;
import com.example.fanatic_coder.mynotepad.callbacks.NoteEventListener;
import com.example.fanatic_coder.mynotepad.model.Note;
import com.example.fanatic_coder.mynotepad.utils.NoteUtils;

import java.util.ArrayList;
import java.util.List;
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

public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Note> notes;
    private Context context;
    private NoteEventListener listener;
    private DeleteThisNoteListener deleteThisNoteListener;
    private boolean isCheckedAll;

    public NotesAdapter(ArrayList<Note> notes, Context context, DeleteThisNoteListener deleteThisNoteListener) {
        this.notes = notes;
        this.context = context;
        this.deleteThisNoteListener = deleteThisNoteListener;
    }

    //method onCreateViewHolder is called when a new view is created
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View v = LayoutInflater.from(context).inflate(R.layout.notes_layout, parent, false);
            return new FirstNoteHolder(v);
        }
        else {
            View v = LayoutInflater.from(context).inflate(R.layout.note2_layout, parent, false);
            return new SecondNoteHolder(v);
        }
    }

    //method onBindViewHolder displays data at specified position
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch(holder.getItemViewType()) {
            case 0: {
                FirstNoteHolder firstNoteHolder = (FirstNoteHolder)holder;
                final Note note = getNote(position);
                if (note != null) {
                    firstNoteHolder.noteTitle.setText(note.getNoteTitle());
                    firstNoteHolder.noteBodyText.setText(note.getNoteBodyText());
                    firstNoteHolder.noteDate.setText(NoteUtils.dateFromLong(note.getNoteDate()));

                    //init note click event
                    firstNoteHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //listener for when clicking on noteBodyText and noteDate.
                            listener.onNoteClick(note);
                        }
                    });

                    //init deleteOnlyNote button click event
                    firstNoteHolder.deleteOnlyNote.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //deleteThisNoteListener object is a listener when clicking on deleteThisNote button and deletes the particular note.
                            deleteThisNoteListener.onDeleteThisNoteClick(note);
                        }
                    });

                    //init each notes checkbox, check event
                    firstNoteHolder.noteCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            note.setChecked(isChecked);
                        }
                    });

                    //Code for the checkbox, for checking all notes (cbAllNotes). Used for first note holder, first note view (notes_layout).
                    if (!isCheckedAll) firstNoteHolder.noteCheck.setChecked(false); //If not all notes are checked, do not check notes checkboxes.
                    else firstNoteHolder.noteCheck.setChecked(true); //Else if all notes are checked, check their checkboxes(noteCheck) too.
                }
            }
            break;
            case 1: {
                final SecondNoteHolder secondNoteHolder = (SecondNoteHolder)holder;
                final Note note = getNote(position);
                if (note != null) {
                    secondNoteHolder.noteTitle.setText(note.getNoteTitle());
                    secondNoteHolder.noteBodyText.setText(note.getNoteBodyText());
                    secondNoteHolder.noteDate.setText(NoteUtils.dateFromLong(note.getNoteDate()));

                    //init note click event
                    secondNoteHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //listener for when clicking on noteBodyText and noteDate.
                            listener.onNoteClick(note);
                        }
                    });

                    //init deleteOnlyNote button click event
                    secondNoteHolder.deleteOnlyNote.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //deleteThisNoteListener object is a listener when clicking on deleteThisNote button and deletes the particular note.
                            deleteThisNoteListener.onDeleteThisNoteClick(note);
                        }
                    });

                    //init each notes checkbox, check event
                    secondNoteHolder.noteCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            note.setChecked(isChecked);
                        }
                    });

                    //Code for the checkbox, for checking all notes (cbAllNotes). Used for second note holder, second note view (note2_layout).
                    if (!isCheckedAll) secondNoteHolder.noteCheck.setChecked(false); //If not all notes are checked, do not check notes checkboxes.
                    else secondNoteHolder.noteCheck.setChecked(true); //Else if all notes are checked, check their checkboxes(noteCheck) too.
                }
            }
            break;
            default:
                break;
        }
    }
    //Method for unselecting/unchecking all notes.
    public void unselectAllNotes() {
        isCheckedAll = false;
        notifyDataSetChanged();
    }

    //Method for selecting/checking all notes.
    public void selectAllNotes() {
        isCheckedAll = true;
        notifyDataSetChanged();
    }

    //First note holder for the first note view.
    class FirstNoteHolder extends RecyclerView.ViewHolder{
        TextView noteTitle, noteBodyText, noteDate;
        //The actual button to delete the note.
        RelativeLayout deleteOnlyNote;
        //The checkbox for each note.
        CheckBox noteCheck;
        //The checkbox which checks/selects all notes.
        CheckBox cbAllNotes;

        FirstNoteHolder(@NonNull View itemView) {
            super(itemView);
            //assigning note Title and finding View by id.
            noteTitle = itemView.findViewById(R.id.noteTitle);
            //assigning note Date and finding View by id.
            noteDate = itemView.findViewById(R.id.noteDate);
            //assigning written note and finding View by id.
            noteBodyText = itemView.findViewById(R.id.noteBodyText);
            //assigning delete Only This Note button and finding View by id.
            deleteOnlyNote = itemView.findViewById(R.id.delete_only_this_note);
            //assigning note check box and finding View by id.
            noteCheck = itemView.findViewById(R.id.noteCheckbox);
            //assigning check box for checking all notes  and finding View by id.
            cbAllNotes = itemView.findViewById(R.id.cbAllNotes);
        }
    }

    //Second note holder for the second note view.
    class SecondNoteHolder extends RecyclerView.ViewHolder{
        TextView noteTitle, noteBodyText, noteDate;
        //The actual button to delete the note.
        RelativeLayout deleteOnlyNote;
        //The checkbox for each note.
        CheckBox noteCheck;
        //The checkbox which checks/selects all notes.
        CheckBox cbAllNotes;

        SecondNoteHolder(@NonNull View itemView) {
            super(itemView);
            //assigning note Title and finding View by id.
            noteTitle = itemView.findViewById(R.id.noteTitle);
            //assigning note Date and finding View by id.
            noteDate = itemView.findViewById(R.id.noteDate);
            //assigning written note and finding View by id.
            noteBodyText = itemView.findViewById(R.id.noteBodyText);
            //assigning delete Only This Note button and finding View by id.
            deleteOnlyNote = itemView.findViewById(R.id.delete_only_this_note);
            //assigning note check box and finding View by id.
            noteCheck = itemView.findViewById(R.id.noteCheckbox);
            //assigning check box for checking all notes  and finding View by id.
            cbAllNotes = itemView.findViewById(R.id.cbAllNotes);
        }
    }

    //Gets all the checked notes in a List and returns the List.
    public List<Note> getCheckedNotes() {
        List<Note> checkedNotes = new ArrayList<>(); //New note List called checkedNotes
        for (Note n : this.notes) { // For every note
            if (n.isChecked()){ // If the note n is checked
                checkedNotes.add(n); //Add the note (n) to the List checkedNotes
            }
        }
        return checkedNotes; //Return List checkedNotes.
    }

    //method getNote gets the notes position
    private Note getNote (int position) {
        return notes.get(position);
    }

    //Gets the note view type. Method for using different note views.
    @Override
    public int getItemViewType(int position) {
        //if note position is an even number then choose first note view
        //if it isnt and it is an odd number then choose second note view
        if (position % 2 == 0) {
            return 0;
        }
        else {
            return 1;
        }
    }

    //method getItemCount gets how many notes will be created
    @Override
    public int getItemCount() {
        return notes.size();
    }

    //method to set all the Listeners
    public void setListener(NoteEventListener listener, DeleteThisNoteListener deleteThisNoteListener) {
        this.listener = listener;
        this.deleteThisNoteListener = deleteThisNoteListener;
    }

}
