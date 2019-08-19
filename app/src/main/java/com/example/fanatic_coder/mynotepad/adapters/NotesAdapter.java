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

                    firstNoteHolder.noteCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            note.setChecked(isChecked);
                        }
                    });

                    if (!isCheckedAll) firstNoteHolder.noteCheck.setChecked(false);
                    else firstNoteHolder.noteCheck.setChecked(true);
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

                    secondNoteHolder.noteCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            note.setChecked(isChecked);
                        }
                    });

                    if (!isCheckedAll) secondNoteHolder.noteCheck.setChecked(false);
                    else secondNoteHolder.noteCheck.setChecked(true);
                }
            }
            break;
            default:
                break;
        }
    }

    public void unselectAllNotes() {
        isCheckedAll = false;
        notifyDataSetChanged();
    }

    public void selectAllNotes() {
        isCheckedAll = true;
        notifyDataSetChanged();
    }

    //Holder class for the whole note
    class FirstNoteHolder extends RecyclerView.ViewHolder{
        TextView noteTitle, noteBodyText, noteDate;
        //The actual button to delete the note
        RelativeLayout deleteOnlyNote;
        CheckBox noteCheck;
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
            noteCheck = itemView.findViewById(R.id.noteCheckbox);
            cbAllNotes = itemView.findViewById(R.id.cbAllNotes);
        }
    }

    class SecondNoteHolder extends RecyclerView.ViewHolder{
        TextView noteTitle, noteBodyText, noteDate;
        //The actual button to delete the note
        RelativeLayout deleteOnlyNote;
        CheckBox noteCheck;
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
            noteCheck = itemView.findViewById(R.id.noteCheckbox);
            cbAllNotes = itemView.findViewById(R.id.cbAllNotes);
        }
    }

    public List<Note> getCheckedNotes() {
        List<Note> checkedNotes = new ArrayList<>();
        for (Note n : this.notes) {
            if (n.isChecked()){
                checkedNotes.add(n);
            }
        }
        return checkedNotes;
    }

    //method getNote gets the notes position
    private Note getNote (int position) {
        return notes.get(position);
    }

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
