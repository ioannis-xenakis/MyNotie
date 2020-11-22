package com.example.fanatic_coder.mynotepad.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.fanatic_coder.mynotepad.R;
import com.example.fanatic_coder.mynotepad.callbacks.MoreMenuButtonListener;
import com.example.fanatic_coder.mynotepad.callbacks.NoteEventListener;
import com.example.fanatic_coder.mynotepad.model.Note;
import com.example.fanatic_coder.mynotepad.utils.NoteUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    public ArrayList<Note> notes;
    public ArrayList<Note> notesFull;
    private Context context;
    private NoteEventListener listener;
    private MoreMenuButtonListener moreMenuButtonListener;

    public NotesAdapter(ArrayList<Note> notes, Context context, MoreMenuButtonListener moreMenuButtonListener) {
        this.notes = notes;
        this.context = context;
        this.moreMenuButtonListener = moreMenuButtonListener;
        notesFull = new ArrayList<>(notes);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.note_layout, parent, false);
        return new NoteHolder(v);
    }

    //Displays data at specified position.
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final NoteHolder noteHolder = (NoteHolder) holder;
        final Note note = getNote(position);
        if (note != null) {
            //Entering text to each Text fields.
            noteHolder.noteTitle.setText(note.getNoteTitle());
            noteHolder.noteBodyText.setText(note.getNoteBodyText());
            noteHolder.noteDate.setText(NoteUtils.dateFromLong(note.getNoteDate()));

            noteHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //When clicking on note(note title, note body text, note date).
                    listener.onNoteClick(note, noteHolder);
                }
            });

            noteHolder.noteCardView.setChecked(note.isChecked());
            noteHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //When long clicking(holding click) on note(note title, note body text, note date).
                    listener.onNoteLongClick(note, noteHolder);
                    return true;
                }
            });

            noteHolder.moreMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moreMenuButtonListener.onMoreMenuButtonClick(note, view, noteHolder.getBindingAdapterPosition());
                }
            });

            //If note title is empty, hide note title from the user, so it wont take any space, on screen.
            if (noteHolder.noteTitle.getText().toString().equals(""))
                noteHolder.noteTitle.setVisibility(View.GONE);

            //If note body text is empty, hide note body text from the user, so it wont take any space, on screen.
            if (noteHolder.noteBodyText.getText().toString().equals(""))
                noteHolder.noteBodyText.setVisibility(View.GONE);

        }
    }

    //Gets the particular note and its position.
    public Note getNote(int position) {
        return notes.get(position);
    }

    //Gets how many notes are there.
    @Override
    public int getItemCount() {
        return notes.size();
    }

    //setListener sets all listeners
    public void setListener(NoteEventListener listener, MoreMenuButtonListener moreMenuButtonListener) {
        this.listener = listener;
        this.moreMenuButtonListener = moreMenuButtonListener;
    }

    public static class NoteHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteBodyText, noteDate;
        MaterialButton moreMenu;
        public MaterialCardView noteCardView;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            noteCardView = itemView.findViewById(R.id.note);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteBodyText = itemView.findViewById(R.id.note_body_text);
            noteDate = itemView.findViewById(R.id.note_date);
            moreMenu = itemView.findViewById(R.id.more_menu_button);
        }

    }

    public List<Note> getCheckedNotes() {
        List<Note> checkedNotes = new ArrayList<>(); //New note List called checkedNotes
        for (Note note : this.notesFull) { // For every note
            if (note.isChecked()){ // If the note n is checked
                checkedNotes.add(note); //Add the note (n) to the List checkedNotes
            }
        }
        return checkedNotes; //Return List checkedNotes.
    }

    public void setAllCheckedNotes(boolean isChecked) {
        for (Note note : this.notes) {
            note.setChecked(isChecked);
        }

        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return notesFilter;
    }

    private final Filter notesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Note> filteredNotesList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredNotesList.addAll(notesFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Note note : notesFull) {
                    if (note.getNoteTitle().toLowerCase().contains(filterPattern) || note.getNoteBodyText().toLowerCase().contains(filterPattern)) {
                        filteredNotesList.add(note);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredNotesList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            notes.clear();
            //noinspection unchecked
            notes.addAll((ArrayList<Note>) filterResults.values);
            notifyDataSetChanged();
        }
    };

}
