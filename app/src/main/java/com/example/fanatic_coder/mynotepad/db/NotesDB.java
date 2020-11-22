
package com.example.fanatic_coder.mynotepad.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.fanatic_coder.mynotepad.model.Note;

@Database(entities = Note.class, version = 1)
public abstract class NotesDB extends RoomDatabase {

    public abstract NotesDAO notesDAO();

    public static final String DATABASE_NAME = "notesDb";

    private static NotesDB instance;

    public static NotesDB getInstance(Context context){
        if( instance == null){
            instance = Room.databaseBuilder(context, NotesDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public static NotesDB getNoSaveInstance(Context context){
        if( instance == null){
            instance = Room.inMemoryDatabaseBuilder(context, NotesDB.class)
                    .build();
        }
        return instance;
    }

}
