package com.code_that_up.john_xenakis.my_notie.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

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
 * <h2>NoteFolderJoin</h2> is a model class and a connection link
 * between a note and a folder and specifies
 * a many-to-many relation, in database.
 * @author John/Ioannis Xenakis
 * @version 1.0
 */
@Entity(
    tableName = "note_folder_join",
    primaryKeys = ["noteId", "folderId"],
    foreignKeys = [ForeignKey(
        entity = Note::class,
        parentColumns = arrayOf("noteId"),
        childColumns = arrayOf("noteId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.SET_NULL
    ), ForeignKey(
        entity = Folder::class,
        parentColumns = arrayOf("folderId"),
        childColumns = arrayOf("folderId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.SET_NULL
    )]
)
class NoteFolderJoin
/**
 * The constructor that constructs and is used for initializing this class.
 * @param noteId The unique id number that specifies each note.
 * @param folderId The unique id number that specifies each folder.
 */(
    /**
     * The unique id number that specifies each note.
     */
    @field:ColumnInfo(
        name = "noteId",
        index = true
    ) val noteId: Int,
    /**
     * The unique id number that specifies each folder.
     */
    @field:ColumnInfo(name = "folderId", index = true) val folderId: Int
)