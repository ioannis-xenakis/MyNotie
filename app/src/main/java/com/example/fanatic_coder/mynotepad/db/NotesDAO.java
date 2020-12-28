package com.example.fanatic_coder.mynotepad.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fanatic_coder.mynotepad.model.Note;

import java.util.List;

/**
 * <h2>NotesDAO</h2> is the Data Object Access for notes,
 * which is responsible for manipulating data and insert, delete and update notes, from database.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see com.example.fanatic_coder.mynotepad.MyNotesActivity the activity/page which calls this java interface(NotesDAO.class) <br>
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

}
