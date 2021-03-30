
package com.code_that_up.john_xenakis.my_notie.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.code_that_up.john_xenakis.my_notie.model.Note;

/**
 * <h2>NotesDB</h2> is the database creator for notes and the unifier for the note model/entity and notes dao.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see androidx.room.RoomDatabase This app, uses Room database
 * @see NotesDAO This class uses NotesDAO.class
 * @see Note This class uses Note.class for entity
 */
@Database(entities = Note.class, version = 1)
public abstract class NotesDB extends RoomDatabase {

    /**
     * The Data Object Access for notes,
     * responsible for manipulating data, in database.
     * @return the notesDAO from <i>NotesDAO.java</i> class.
     */
    public abstract NotesDAO notesDAO();

    /**
     * The name of the database.
     */
    public static final String DATABASE_NAME = "notesDb";

    /**
     * The notes database and its instance.
     */
    private static NotesDB instance;

    /**
     * getInstance, gets the created instance/the notes database.
     * @param context the context, containing all the data/content of an activity.
     * @return the instance/created database.
     */
    public static NotesDB getInstance(Context context){
        if( instance == null){
            instance = Room.databaseBuilder(context, NotesDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    /**
     * getNoSaveInstance, gets the created instance/the notes database,
     * but gets saved only in memory and gets erased after app ends.
     * @param context the context, containing all the data/content of an activity.
     * @return the instance/created only in memory, database.
     */
    public static NotesDB getNoSaveInstance(Context context){
        if( instance == null){
            instance = Room.inMemoryDatabaseBuilder(context, NotesDB.class)
                    .build();
        }
        return instance;
    }

}
