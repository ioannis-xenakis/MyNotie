package com.code_that_up.john_xenakis.my_notie.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.code_that_up.john_xenakis.my_notie.callbacks.MoreMenuButtonListener;
import com.code_that_up.john_xenakis.my_notie.callbacks.NoteEventListener;
import com.code_that_up.john_xenakis.my_notie.model.Note;
import com.code_that_up.john_xenakis.my_notie.utils.NoteUtils;
import com.code_that_up.john_xenakis.my_notie.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

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
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            notes.clear();
            //noinspection unchecked
            notes.addAll((ArrayList<Note>) filterResults.values);
            for (Note note : notesFull) {
                notifyItemChanged(notesFull.indexOf(note));
            }
        }
    };

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

}