package com.code_that_up.john_xenakis.my_notie.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.code_that_up.john_xenakis.my_notie.model.Folder;

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

/**
 * <h2>FoldersDAO</h2> is the Data Object Access for folders,
 * which is responsible for manipulating data and insert, delete and update folders, from database.
 * @author John/Ioannis Xenakis
 * @version 1.0
 */
@Dao
public interface FoldersDAO {

    /**
     * insertFolder, inserts/adds a new folder to the database.
     * @param folder The new folder.
     */
    @Insert
    void insertFolder(Folder folder);

    /**
     * deleteFolder, deletes a folder from database.
     * @param folder The folder, to be deleted.
     */
    @Delete
    void deleteFolder(Folder folder);

    /**
     * updateFolder, updates an existing folder, from database.
     * @param folder The folder, to be updated.
     */
    @Update
    void updateFolder(Folder folder);

    /**
     * Gets all folders, from database.
     * @return All existing folders.
     */
    @Query("SELECT * FROM folders")
    List<Folder> getAllFolders();

    /**
     * Gets the last/higher folder Id number.
     * @return The last/higher folder Id number.
     */
    @Query("SELECT MAX(folderId) FROM folders")
    int getMaxFolderId();

    /**
     * Gets all the folder Id's in a list.
     * @return The folder Id's list.
     */
    @Query("SELECT folderId FROM folders")
    List<Integer> getListOfFolderIds();

}