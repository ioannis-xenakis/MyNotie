package com.code_that_up.john_xenakis.my_notie.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.code_that_up.john_xenakis.my_notie.model.Folder

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
 * <h2>FoldersDAO</h2> is the Data Object Access for folders,
 * which is responsible for manipulating data and insert, delete and update folders, from database.
 * @author John/Ioannis Xenakis
 * @version 1.0
 */
@Dao
interface FoldersDAO {
    /**
     * insertFolder, inserts/adds a new folder to the database.
     * @param folder The new folder.
     */
    @Insert
    fun insertFolder(folder: Folder)

    /**
     * deleteFolder, deletes a folder from database.
     * @param folder The folder, to be deleted.
     */
    @Delete
    fun deleteFolder(folder: Folder)

    /**
     * updateFolder, updates an existing folder, from database.
     * @param folder The folder, to be updated.
     */
    @Update
    fun updateFolder(folder: Folder)

    /**
     * Gets all folders, from database.
     * @return All existing folders.
     */
    @get:Query("SELECT * FROM folders")
    val allFolders: List<Folder?>?

    /**
     * Gets the last/higher folder Id number.
     * @return The last/higher folder Id number.
     */
    @get:Query("SELECT MAX(folderId) FROM folders")
    val maxFolderId: Int

    /**
     * Gets all the folder Id's in a list.
     * @return The folder Id's list.
     */
    @get:Query("SELECT folderId FROM folders")
    val listOfFolderIds: List<Int?>?
}