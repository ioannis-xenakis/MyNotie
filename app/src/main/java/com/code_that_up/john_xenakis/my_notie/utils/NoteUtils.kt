package com.code_that_up.john_xenakis.my_notie.utils

import com.code_that_up.john_xenakis.my_notie.db.NotesDAO
import com.code_that_up.john_xenakis.my_notie.model.Note
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
 * <h2>NoteUtils</h2> is a class, which contains utilities/tools for notes.
 * @author John/Ioannis Xenakis
 * @version 1.0
 */
object NoteUtils {
    /**
     * dateFromLong, creates and returns a date when the note is created/edited.
     * @param time the time/date the note is created/edited.
     * @return the time/date, the note is created/edited.
     */
    fun dateFromLong(time: Long): String {
        val format: DateFormat =
            SimpleDateFormat("'Last edited: ' dd/MMM/yyyy ' ' hh:mm", Locale.US)
        return format.format(Date(time))
    }

    /**
     * Increases a Notes id number by one.
     * @param noteDao The Data Object Access for notes which is responsible for manipulating data, from database.
     * @param note The note that its id, is incremented.
     */
    @JvmStatic
    fun increaseNoteIdByOne(noteDao: NotesDAO, note: Note) {
        if (!noteDao.listOfNoteIds!!.contains(noteDao.maxNoteId + 1)) {
            note.id = noteDao.maxNoteId + 1
        } else {
            var increasedNoteId = noteDao.maxNoteId + 1
            while (noteDao.listOfNoteIds!!.contains(increasedNoteId)) {
                increasedNoteId++
            }
            note.id = increasedNoteId
        }
    }
}