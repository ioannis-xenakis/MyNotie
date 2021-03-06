//Notes Data Object Access (NotesDAO) to manipulate data and insert, delete and update notes
package com.example.fanatic_coder.mynotepad.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fanatic_coder.mynotepad.model.Note;

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

@Dao
public interface NotesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //If the note exists, replace it.
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Update
    void updateNote(Note note);

    // Show all notes from database
    @Query("SELECT * FROM notes")
    List<Note> getNotes();

    @Query("SELECT * FROM notes WHERE id = :noteId") // Get the notes by their id
    Note getNoteById(int noteId);

    @Query("DELETE FROM notes WHERE id = :noteId") // Delete notes by their id
    void deleteNoteById(int noteId);

}
