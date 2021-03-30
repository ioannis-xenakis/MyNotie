package com.code_that_up.john_xenakis.my_notie.callbacks;

import com.code_that_up.john_xenakis.my_notie.MyNotesActivity;
import com.code_that_up.john_xenakis.my_notie.adapters.NotesAdapter;
import com.code_that_up.john_xenakis.my_notie.model.Note;

/**
 * <h2>NoteEventListener</h2> is a listener for listening clicks on note, used on MyNotesActivity.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see MyNotesActivity the activity/page which calls this java interface(NoteEventListener.java) <br>
 * @see <a href="https://github.com/ioannis-xenakis/MyNotie">https://github.com/ioannis-xenakis/MyNotie</a> This project, is uploaded at Github. Visit it if you want!
 */
public interface NoteEventListener {

    /**
     * onNoteClick, runs/is called, when note is clicked.
     * @param note the note.
     * @param noteHolder the holder that holds content/buttons in a note.
     */
    void onNoteClick(Note note, NotesAdapter.NoteHolder noteHolder);

    /**
     * onNoteLongClick, runs/is called, when note is <b>long</b> clicked
     * @param note the note.
     * @param noteHolder the holder that holds content/buttons in a note.
     */
    void onNoteLongClick(Note note, NotesAdapter.NoteHolder noteHolder);

}
