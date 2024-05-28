package com.code_that_up.john_xenakis.my_notie.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomWarnings
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
 * <h2>NotesFoldersJoinDAO</h2> is the Data Object Access for the connection link between folders and notes,
 * is responsible for manipulating data from database and is depending on a many-to-many database relationship
 * (1 folder has many notes and 1 note has many folders).
 * @author John/Ioannis Xenakis
 * @version 1.0
 */
@Dao
interface NotesFoldersJoinDAO {
    /**
     * Inserts/adds a new/replaces an existent noteFolderJoin(a link between a note and a folder).
     * @param noteFolderJoin The connection link between a note and a folder. The link that is added.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNoteFolderJoin(noteFolderJoin: NoteFolderJoin)

    /**
     * Deletes an existent noteFolderJoin(a link between a note and a folder).
     * @param noteFolderJoin The connection link between a note and a folder. The link that is deleted.
     */
    @Delete
    fun deleteNoteNoteFolderJoin(noteFolderJoin: NoteFolderJoin)

    /**
     * Gets notes *only* from a specific folder.
     * @param folderId The unique folder id number for choosing the folder.
     * @return The notes list from the folder.
     */
    @Query("SELECT * FROM notes INNER JOIN note_folder_join ON notes.noteId=note_folder_join.noteId WHERE note_folder_join.folderId=:folderId")
    fun getNotesFromFolder(folderId: Int): List<Note>?

    /**
     * Gets only the folders that is associated/connected with 1 note, that is specified by the note Id number.
     * @param noteId The unique id of the note, that is associated with the folders.
     * @return A list of folders, that is associated with the note.
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM folders INNER JOIN note_folder_join ON folders.folderId=note_folder_join.folderId WHERE note_folder_join.noteId=:noteId")
    fun getFoldersFromNote(noteId: Int?): List<Folder>
}