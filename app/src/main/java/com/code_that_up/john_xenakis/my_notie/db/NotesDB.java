
package com.code_that_up.john_xenakis.my_notie.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.code_that_up.john_xenakis.my_notie.model.Folder;
import com.code_that_up.john_xenakis.my_notie.model.Note;
import com.code_that_up.john_xenakis.my_notie.model.NoteFolderJoin;
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
 * <h2>NotesDB</h2> is the database creator for notes and folders
 * and the unifier for the note model/entity, folder model/entity, notes dao and folders dao.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see androidx.room.RoomDatabase This app, uses Room database
 * @see NotesDAO This class uses NotesDAO.class
 * @see Note This class uses Note.class for entity
 */
@Database(entities = {Note.class, Folder.class, NoteFolderJoin.class}, version = 1)
public abstract class NotesDB extends RoomDatabase {

    /**
     * The Data Object Access for notes,
     * responsible for manipulating data, in database.
     * @return the notesDAO from <i>NotesDAO.java</i> class.
     */
    public abstract NotesDAO notesDAO();

    /**
     * The Data Object Access for folders,
     * responsible for manipulating data, in database.
     * @return The foldersDAO from <i>FoldersDAO.java</i> class.
     */
    public abstract FoldersDAO foldersDAO();

    /**
     * The Data Access Object for the connection link between notes and folders,
     * is responsible for manipulating data, in database and is depending on a many-to-many database relationship
     * (1 folder has many notes and 1 note has many folders).
     * @return The NotesFoldersJoinDAO from <i>NotesFoldersJoinDAO.java</i> interface.
     */
    public abstract NotesFoldersJoinDAO notesFoldersJoinDAO();

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
