//Notes Data Object Access (NotesDAO) to manipulate data and insert, delete and update notes
package com.example.fanatic_coder.mynotepad.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.fanatic_coder.mynotepad.model.Note;

import java.util.ArrayList;

@Dao
public interface NotesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //If the note exists, replace it.
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Update
    void updateNote(Note note);

    @Query("SELECT * FROM notes") // Show all notes from database
    ArrayList<Note> getNotes();

    @Query("SELECT * FROM notes WHERE id = :noteId") // Get the notes by their id
    Note getNoteById(int noteId);

    @Query("DELETE FROM notes WHERE id = :noteId") // Delete notes by their id
    void deleteNoteById(int noteId);

}
