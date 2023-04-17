package com.code_that_up.john_xenakis.my_notie.utils

import com.code_that_up.john_xenakis.my_notie.db.FoldersDAO
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
 * <h2>FolderUtils</h2> is a class, which contains utilities/tools for folders.
 * @author John/Ioannis Xenakis
 * @version 1.0
 */
object FolderUtils {
    /**
     * Increases a folder id number by one.
     * @param folderDao The Data Object Access for folders, responsible for managing folder data, in database.
     * @param folder The folder that its id, is incremented.
     */
    @JvmStatic
    fun increaseFolderIdByOne(folderDao: FoldersDAO, folder: Folder) {
        if (!folderDao.listOfFolderIds!!.contains(folderDao.maxFolderId + 1)) {
            folder.id = folderDao.maxFolderId + 1
        } else {
            var increasedFolderId = folderDao.maxFolderId + 1
            while (folderDao.listOfFolderIds!!.contains(increasedFolderId)) {
                increasedFolderId++
            }
            folder.id = increasedFolderId
        }
    }
}