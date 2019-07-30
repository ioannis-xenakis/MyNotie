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
