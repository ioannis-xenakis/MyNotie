package com.code_that_up.john_xenakis.my_notie.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.code_that_up.john_xenakis.my_notie.db.NotesDB.MyAutoMigration
import com.code_that_up.john_xenakis.my_notie.model.Folder
import com.code_that_up.john_xenakis.my_notie.model.Note
import com.code_that_up.john_xenakis.my_notie.model.NoteFolderJoin

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
 *
 * @see NotesDAO This class uses NotesDAO.class
 *
 * @see Note This class uses Note.class for entity
 */
@Database(
    entities = [Note::class, Folder::class, NoteFolderJoin::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2, spec = MyAutoMigration::class)]
)
abstract class NotesDB : RoomDatabase() {
    /**
     * The Data Object Access for notes,
     * responsible for manipulating data, in database.
     * @return the notesDAO from *NotesDAO.java* class.
     */
    abstract fun notesDAO(): NotesDAO?

    /**
     * The Data Object Access for folders,
     * responsible for manipulating data, in database.
     * @return The foldersDAO from *FoldersDAO.java* class.
     */
    abstract fun foldersDAO(): FoldersDAO?

    /**
     * The Data Access Object for the connection link between notes and folders,
     * is responsible for manipulating data, in database and is depending on a many-to-many database relationship
     * (1 folder has many notes and 1 note has many folders).
     * @return The NotesFoldersJoinDAO from *NotesFoldersJoinDAO.java* interface.
     */
    abstract fun notesFoldersJoinDAO(): NotesFoldersJoinDAO?

    @RenameColumn(tableName = "notes", fromColumnName = "id", toColumnName = "noteId")
    internal class MyAutoMigration : AutoMigrationSpec
    companion object {
        /**
         * The name of the database.
         */
        const val DATABASE_NAME = "notesDb"

        /**
         * The notes database and its instance.
         */
        private var instance: NotesDB? = null

        /**
         * getInstance, gets the created instance/the notes database.
         * @param context the context, containing all the data/content of an activity.
         * @return the instance/created database.
         */
        @JvmStatic
        fun getInstance(context: Context?): NotesDB? {
            if (instance == null) {
                instance = Room.databaseBuilder(context!!, NotesDB::class.java, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }

        /**
         * getNoSaveInstance, gets the created instance/the notes database,
         * but gets saved only in memory and gets erased after app ends.
         * @param context the context, containing all the data/content of an activity.
         * @return the instance/created only in memory, database.
         */
        @JvmStatic
        fun getNoSaveInstance(context: Context?): NotesDB? {
            if (instance == null) {
                instance = Room.inMemoryDatabaseBuilder(context!!, NotesDB::class.java)
                    .build()
            }
            return instance
        }
    }
}