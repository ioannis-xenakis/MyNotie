package com.code_that_up.john_xenakis.my_notie.callbacks

import com.code_that_up.john_xenakis.my_notie.adapters.NotesAdapter.NoteHolder
import com.code_that_up.john_xenakis.my_notie.model.Note

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
 * <h2>NoteEventListener</h2> is a listener for listening clicks on note, used on MyNotesActivity.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see [https://github.com/ioannis-xenakis/MyNotie](https://github.com/ioannis-xenakis/MyNotie) This project, is uploaded at Github. Visit it if you want!
 */
interface NoteEventListener {
    /**
     * onNoteClick, runs/is called, when note is clicked.
     * @param note the note.
     * @param noteHolder the holder that holds content/buttons in a note.
     */
    fun onNoteClick(note: Note?, noteHolder: NoteHolder?)

    /**
     * onNoteLongClick, runs/is called, when note is **long** clicked
     * @param note the note.
     * @param noteHolder the holder that holds content/buttons in a note.
     */
    fun onNoteLongClick(note: Note?, noteHolder: NoteHolder?)
}