package com.example.fanatic_coder.mynotepad.callbacks;

import com.example.fanatic_coder.mynotepad.model.Note;

public interface NoteEventListener {

    /**
     *
     * @param note call onNoteClick when note clicked
     */
    void onNoteClick(Note note);

}
