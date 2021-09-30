package com.code_that_up.john_xenakis.my_notie.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.code_that_up.john_xenakis.my_notie.MyNotesActivity;
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
 * <h2>NotesDAO</h2> is the Data Object Access for notes,
 * which is responsible for manipulating data and insert, delete and update notes, from database.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see MyNotesActivity the activity/page which calls this java interface(NotesDAO.class) <br>
 */
@Dao
public interface NotesDAO {

    /**
     * insertNote, inserts/adds new note to database.
     * @param note the note.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE) //If the note exists, replace it.
    void insertNote(Note note);

    /**
     * deleteNote, deletes a note from database.
     * @param note the note.
     */
    @Delete
    void deleteNote(Note note);

    /**
     * updateNote, updates an existing note from database.
     * @param note the note.
     */
    @Update
    void updateNote(Note note);

    /**
     * Gets all notes from database.
     * @return All existing notes.
     */
    @Query("SELECT * FROM notes")
    List<Note> getNotes();

    /**
     * Gets a specific note, by specifying its id.
     * @param noteId the note id.
     * @return the note.
     */
    @Query("SELECT * FROM notes WHERE noteId = :noteId")
    Note getNoteById(int noteId);

    /**
     * Gets the last/highest note Id number.
     * @return The last/highest note Id number.
     */
    @Query("SELECT MAX(noteId) FROM notes")
    int getMaxNoteId();

    /**
     * Gets all the folder Ids in a list.
     * @return The folder Ids list.
     */
    @Query("SELECT noteId FROM notes")
    List<Integer> getListOfNoteIds();

}
