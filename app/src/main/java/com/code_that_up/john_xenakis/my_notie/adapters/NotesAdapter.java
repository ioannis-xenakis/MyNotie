package com.code_that_up.john_xenakis.my_notie.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.code_that_up.john_xenakis.my_notie.callbacks.MoreMenuButtonListener;
import com.code_that_up.john_xenakis.my_notie.callbacks.NoteEventListener;
import com.code_that_up.john_xenakis.my_notie.db.NotesDB;
import com.code_that_up.john_xenakis.my_notie.model.Folder;
import com.code_that_up.john_xenakis.my_notie.model.Note;
import com.code_that_up.john_xenakis.my_notie.utils.NoteDiffCallback;
import com.code_that_up.john_xenakis.my_notie.utils.NoteUtils;
import com.code_that_up.john_xenakis.my_notie.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

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

/**
 * <h2>NotesAdapter</h2> is the adapter and the <i>middle man</i> between the notes ui and the actual data to be set in notes.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see <a href="https://github.com/ioannis-xenakis/MyNotie">https://github.com/ioannis-xenakis/MyNotie</a> This project, is uploaded at Github. Visit it if you want!
 */
public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    /**
     * The notes list, which is <b>only displayed</b> at the screen.
     */
    public ArrayList<Note> notes;

    /**
     * The notes list, which is displayed <b>regardless</b> of being at the screen.
     */
    public ArrayList<Note> notesFull;

    /**
     * The current state and <b>context</b> of the notes.
     */
    private final Context context;

    /**
     * The NoteEventListener for listening clicks, on a note, in notes list.
     */
    private NoteEventListener listener;

    /**
     * The listener for listening clicks, on the <i>more menu button</i> (three dots icon button), which is on every note, in notes list.
     */
    private MoreMenuButtonListener moreMenuButtonListener;

    /**
     * The constructor to need, when initializing/creating this <i>NotesAdapter</i> class file.
     * @param notes the notes list.
     * @param context the context/current state of notes.
     * @param moreMenuButtonListener the listener for listening clicks, on <i>more menu button</i> (three dots icon button), which is on every note, in notes list.
     */
    public NotesAdapter(ArrayList<Note> notes, Context context, MoreMenuButtonListener moreMenuButtonListener) {
        this.notes = notes;
        this.context = context;
        this.moreMenuButtonListener = moreMenuButtonListener;
        notesFull = new ArrayList<>(notes);
    }

    /**
     * onCreateViewHolder gets called/runs, when a note is <b>created</b>.
     * @param parent the note/group that nests the buttons/content of the note.
     * @param viewType the type of note.
     * @return a new note holder, that holds new content/buttons in note.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.note_layout, parent, false);
        return new NoteHolder(v);
    }

    /**
     * onBindViewHolder, binds the needed data or make actions to each button/element, on each note and displays each note to the proper position.
     * @param holder The holder which holds all the content/buttons, in each note.
     * @param position the position of each note, in notes list.
     */
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final NoteHolder noteHolder = (NoteHolder) holder;
        final Note note = getNote(position);
        if (note != null) {
            //Entering text to each Text fields.
            noteHolder.noteTitle.setText(note.getNoteTitle());
            noteHolder.noteBodyText.setText(note.getNoteBodyText());
            noteHolder.noteDate.setText(NoteUtils.dateFromLong(note.getNoteDate()));

            //When clicking on note(note title, note body text, note date).
            noteHolder.itemView.setOnClickListener(view -> listener.onNoteClick(note, noteHolder));

            noteHolder.noteCardView.setChecked(note.isChecked());
            //When long clicking(holding click) on note(note title, note body text, note date).
            noteHolder.itemView.setOnLongClickListener(view -> {
                listener.onNoteLongClick(note, noteHolder);
                return true;
            });

            noteHolder.moreMenu.setOnClickListener(view ->
                    moreMenuButtonListener.onMoreMenuButtonClick(note, view, noteHolder.getBindingAdapterPosition()));

            hideNoteTitleIfEmpty(noteHolder);
            hideNoteBodyTextIfEmpty(noteHolder);

            addFolderChipsAtEachNote(noteHolder, note);
        }
    }

    /**
     * getNote, gets a <b>specific</b> note from notes list.
     * @param position the position of the note, in notes list.
     * @return the note, depending on its position.
     */
    public Note getNote(int position) {
        return notes.get(position);
    }

    /**
     * getItemCount, gets and <b>counts</b> all the existing notes, from notes list.
     * @return the counted notes, from notes list.
     */
    @Override
    public int getItemCount() {
        return notes.size();
    }

    /**
     * setListener, sets all listeners for each note.
     * @param listener The NoteEventListener for listening clicks, on a note, in notes list.
     * @param moreMenuButtonListener The listener for listening clicks, on the <i>more menu button</i> (three dots icon button), which is on every note, in notes list.
     */
    public void setListener(NoteEventListener listener, MoreMenuButtonListener moreMenuButtonListener) {
        this.listener = listener;
        this.moreMenuButtonListener = moreMenuButtonListener;
    }

    /**
     * Sets/replaces note list with a new note list, which is displayed on screen.
     * @param newNoteList The new note list to replace note list.
     */
    public void setNotes(ArrayList<Note> newNoteList) {
        this.notes = newNoteList;
        notesFull = new ArrayList<>(newNoteList);
    }

    /**
     * NoteHolder class, holds all the content/buttons, in a note.
     */
    public static class NoteHolder extends RecyclerView.ViewHolder {
        /**
         * Note title, that the user writes.<br>
         * Note body text, or the main text of note, that the user writes.<br>
         * Note date is the date which the note has been created/last edited.
         */
        TextView noteTitle, noteBodyText, noteDate;

        /**
         * moreMenu button (three vertical dots icon), that diplays the vertical dropdown menu when clicked.
         */
        MaterialButton moreMenu;

        /**
         * Chip group that displays all folders(chips), in a note.
         */
        ChipGroup folderChipGroup;

        /**
         * noteCardView is the note itself, that contains all the content/buttons.
         */
        public MaterialCardView noteCardView;

        /**
         * The constructor with initializing note content/buttons, by their ids.
         * @param itemView the note.
         */
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            noteCardView = itemView.findViewById(R.id.note);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteBodyText = itemView.findViewById(R.id.note_body_text);
            noteDate = itemView.findViewById(R.id.note_date);
            moreMenu = itemView.findViewById(R.id.more_menu_button);
            folderChipGroup = itemView.findViewById(R.id.folder_chip_group);
        }

    }

    /**
     * getCheckedNotes, <b>gets</b> all the already <b>checked/selected</b> notes.
     * @return Checked/selected notes.
     */
    public List<Note> getCheckedNotes() {
        List<Note> checkedNotes = new ArrayList<>(); //New note List called checkedNotes
        for (Note note : this.notesFull) { // For every note
            if (note.isChecked()){ // If the note n is checked
                checkedNotes.add(note); //Add the note (n) to the List checkedNotes
            }
        }
        return checkedNotes; //Return List checkedNotes.
    }

    /**
     * setAllCheckedNotes, <b>selects/checks</b> or <b>unselects/unchecks</b> all <i>displayed on screen</i> notes.
     * @param isChecked The checked/selected or unchecked/unselected state of notes.
     */
    public void setAllCheckedNotes(boolean isChecked) {
        for (Note note : this.notes) {
            note.setChecked(isChecked);
            notifyItemChanged(notes.indexOf(note));
        }
    }

    /**
     * getFilter, gets the filter for filtering displayed notes, when the user searches notes.
     * @return the filter for displaying filtered notes.
     */
    @Override
    public Filter getFilter() {
        return notesFilter;
    }

    /**
     * notesFilter, is the filter for filtering displayed notes, when user searches for notes.
     */
    private final Filter notesFilter = new Filter() {

        /**
         * performFiltering, performs the main mechanism for filtering displayed notes.
         * @param charSequence The text, typed by user.
         * @return the final filtered notes.
         */
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

        /**
         * publishResults, <b>displays</b> the final filtered notes to screen.
         * @param charSequence the user typed text.
         * @param filterResults the final filtered notes.
         */
        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            //noinspection unchecked
            updateNoteList((ArrayList<Note>) filterResults.values);
        }
    };

    /**
     * Updates/refreshes the <i>note list(RecyclerView)</i>.
     * @param newNoteList The new note list containing the new data, to refresh/update to.
     */
    public void updateNoteList(List<Note> newNoteList) {
        final NoteDiffCallback diffCallback = new NoteDiffCallback(this.notes, newNoteList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.notes.clear();
        this.notes.addAll(newNoteList);
        diffResult.dispatchUpdatesTo(this);
    }

    /**
     * hideNoteTitleIfEmpty, hides Note Title from the screen if it is empty
     * and takes less android device resources.
     * @param noteHolder The holder for all the views inside each note.
     */
    private void hideNoteTitleIfEmpty(NoteHolder noteHolder) {
        noteHolder.noteTitle.setVisibility(View.VISIBLE);
        if (noteHolder.noteTitle.getText().toString().equals(""))
            noteHolder.noteTitle.setVisibility(View.GONE);
    }

    /**
     * hideNoteBodyTextIfEmpty, hides Note Body Text from the screen if it is empty
     * and takes less android device resources.
     * @param noteHolder The holder for all the views inside each note.
     */
    private void hideNoteBodyTextIfEmpty(NoteHolder noteHolder) {
        noteHolder.noteBodyText.setVisibility(View.VISIBLE);
        if (noteHolder.noteBodyText.getText().toString().equals(""))
            noteHolder.noteBodyText.setVisibility(View.GONE);
    }

    /**
     * Add folder chips that is connected with each note.
     */
    private void addFolderChipsAtEachNote(NoteHolder noteHolder, Note note) {
        List<Folder> foldersFromNoteList = NotesDB.getInstance(context).notesFoldersJoinDAO().getFoldersFromNote(note.getId());

        noteHolder.folderChipGroup.removeAllViews();
        for (Folder folder : foldersFromNoteList) {
            noteHolder.folderChipGroup.addView(new Chip(context) {{
                setId(ViewCompat.generateViewId());
                setText(folder.getFolderName().trim());
                setContentDescription("Folder chip");
                setCheckable(false);
                setClickable(false);
            }});
        }
    }

}
