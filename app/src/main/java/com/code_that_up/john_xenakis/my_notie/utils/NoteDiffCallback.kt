package com.code_that_up.john_xenakis.my_notie.utils

import androidx.recyclerview.widget.DiffUtil
import com.code_that_up.john_xenakis.my_notie.model.Folder
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
 * The payload/change in a note.
 */
sealed interface NoteChangePayload {
    /**
     * The changed note title.
     * @param newNoteTitle The new note title.
     */
    data class NoteTitle(val newNoteTitle: String?): NoteChangePayload

    /**
     * The changed note body text.
     * @param newNoteBodyText The new note body text.
     */
    data class NoteBodyText(val newNoteBodyText: String?): NoteChangePayload

    /**
     * The changed note date "edited".
     * @param newNoteDateEdited The new note date "edited".
     */
    data class NoteDateEdited(val newNoteDateEdited: Long?): NoteChangePayload
}

/**
 * <h2>NoteDiffCallback</h2> is a class, which is responsible for refreshing and animating the notes with DiffUtil library.
 * Especially detecting note changes between the old/previous note list state and the new note list state and refreshes/animates accordingly depending on the changes.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see [https://github.com/ioannis-xenakis/MyNotie](https://github.com/ioannis-xenakis/MyNotie) This project, is uploaded at Github. Visit it if you want!
 */
class NoteDiffCallback
(
    /**
     * The old/previous note list state, before refreshing.
     */
    private val oldNoteList: List<Note>,
    /**
     * The new note list state, after refreshing.
     */
    private val newNoteList: List<Note>?,

    /**
     * The old folders for each note.
     */
    private val foldersFromOldNote: ArrayList<List<Folder>> = arrayListOf(),

    /**
     * The new folders for each note.
     */
    private val foldersFromNewNote: ArrayList<List<Folder>> = arrayListOf()
) : DiffUtil.Callback() {
    /**
     * Gets the size number of the old/previous note list or how many notes are left in old note list.
     * @return The size number of the old/previous note list.
     */
    override fun getOldListSize(): Int {
        return oldNoteList.size
    }

    /**
     * Gets the size number of the new note list or how many notes are left in new note list.
     * @return The size number of the new note list.
     */
    override fun getNewListSize(): Int {
        return newNoteList!!.size
    }

    /**
     * Gets the boolean(yes/no true/false) state of, if the old and new items(notes) from the old and new lists are the same.
     * @param oldItemPosition The old item(note) position number.
     * @param newItemPosition The new item(note) position number.
     * @return The boolean(yes/no true/false) state of, if the old note is the same or not with the new note.
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNoteList[oldItemPosition].id == newNoteList?.get(newItemPosition)?.id
    }

    /**
     * Gets the boolean(yes/no true/false) state of, if the *contents* of the old and new items(notes) are the same.
     * @param oldItemPosition The old item(note) position number.
     * @param newItemPosition The new item(note) position number.
     * @return The boolean(yes/no true/false) state of, if *contents* of the old and new items(notes) are the same.
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = oldNoteList[oldItemPosition]
        val newNote = newNoteList?.get(newItemPosition)
        var oldFoldersFromNote = listOf<Folder>()
        var newFoldersFromNote = listOf<Folder>()
        if (foldersFromOldNote.isNotEmpty() && foldersFromNewNote.isNotEmpty()) {
            oldFoldersFromNote = foldersFromOldNote[oldItemPosition]
            newFoldersFromNote = foldersFromNewNote[newItemPosition]
        }

        return oldNote.noteTitle == newNote?.noteTitle && oldNote.noteBodyText == newNote?.noteBodyText
                && newFoldersFromNote == oldFoldersFromNote
    }

    /**
     * Gets the change/payload in a note.
     * @param oldNotePosition The position of the old note.
     * @param newNotePosition The position of the new note.
     */
    override fun getChangePayload(oldNotePosition: Int, newNotePosition: Int): Any? {
        val oldNote = oldNoteList[oldNotePosition]
        val newNote = newNoteList?.get(newNotePosition)
        return when {
            oldNote.noteTitle != newNote?.noteTitle -> {
                NoteChangePayload.NoteTitle(newNote?.noteTitle)
            }

            oldNote.noteBodyText != newNote?.noteBodyText -> {
                NoteChangePayload.NoteBodyText(newNote?.noteBodyText)
            }

            oldNote.noteDate != newNote?.noteDate -> {
                NoteChangePayload.NoteDateEdited(newNote?.noteDate)
            }

            else -> super.getChangePayload(oldNotePosition, newNotePosition)
        }
    }
}