package com.code_that_up.john_xenakis.my_notie.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
 * <h2>Folder</h2> is a model class, for managing folder data
 * and how each folder should be structured, in database.
 * @author John/Ioannis Xenakis
 * @version 1.0
 */
@Entity(tableName = "folders")
class Folder
{
    /**
     * The primary key and the id number, for identifying an unique folder, in *folders* entity/table.
     */
    @ColumnInfo(name = "folderId")
    @PrimaryKey
    var id = 0

    /**
     * The folder name, written by user.
     */
    var folderName: String? = null

    /**
     * The checked state of the checkbox in the folder.
     */
    var checked = false
}