package com.code_that_up.john_xenakis.my_notie.utils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.code_that_up.john_xenakis.my_notie.model.Note;

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
 * <h2>NoteDiffCallback</h2> is a class, which is responsible for refreshing and animating the notes with DiffUtil library.
 * Especially detecting note changes between the old/previous note list state and the new note list state and refreshes/animates accordingly depending on the changes.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see <a href="https://github.com/ioannis-xenakis/MyNotie">https://github.com/ioannis-xenakis/MyNotie</a> This project, is uploaded at Github. Visit it if you want!
 */
public class NoteDiffCallback extends DiffUtil.Callback {

    /**
     * The old/previous note list state, before refreshing.
     */
    private final List<Note> oldNoteList;

    /**
     * The new note list state, after refreshing.
     */
    private final List<Note> newNoteList;

    /**
     * The constructor needed, for initializing/creating this <i>NoteDiffCallback</i> class.
     * @param oldNoteList The old/previous note list state before refreshing.
     * @param newNoteList The new note list state, after refreshing.
     */
    public NoteDiffCallback(List<Note> oldNoteList, List<Note> newNoteList) {
        this.oldNoteList = oldNoteList;
        this.newNoteList = newNoteList;
    }

    /**
     * Gets the size number of the old/previous note list or how many notes are left in old note list.
     * @return The size number of the old/previous note list.
     */
    @Override
    public int getOldListSize() {
        return oldNoteList.size();
    }

    /**
     * Gets the size number of the new note list or how many notes are left in new note list.
     * @return The size number of the new note list.
     */
    @Override
    public int getNewListSize() {
        return newNoteList.size();
    }

    /**
     * Gets the boolean(yes/no true/false) state of, if the old and new items(notes) from the old and new lists are the same.
     * @param oldItemPosition The old item(note) position number.
     * @param newItemPosition The new item(note) position number.
     * @return The boolean(yes/no true/false) state of, if the old note is the same or not with the new note.
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldNoteList.get(oldItemPosition).getId() == newNoteList.get(newItemPosition).getId();
    }

    /**
     * Gets the boolean(yes/no true/false) state of, if the <i>contents</i> of the old and new items(notes) are the same.
     * @param oldItemPosition The old item(note) position number.
     * @param newItemPosition The new item(note) position number.
     * @return The boolean(yes/no true/false) state of, if <i>contents</i> of the old and new items(notes) are the same.
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Note oldNote = oldNoteList.get(oldItemPosition);
        final Note newNote = newNoteList.get(newItemPosition);
        return oldNote.getNoteTitle().equals(newNote.getNoteTitle());
    }

    /**
     * Gets the notes contents change(payload object) between an old and new note,
     * if the old and new note are the same and their contents are not.
     * @param oldItemPosition The position number of the old item(note).
     * @param newItemPosition The position number of the new item(note).
     * @return The change(payload object) between old and new note.
     */
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
