//Notes Data Object Access (NotesDAO) to manipulate data and insert, delete and update notes
package com.example.fanatic_coder.mynotepad.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.example.fanatic_coder.mynotepad.model.Note;

@Dao
public interface NotesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //If the note exists, replace it.
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Update
    void updateNote(Note note);
}
