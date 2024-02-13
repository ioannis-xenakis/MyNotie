package com.code_that_up.john_xenakis.my_notie

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.code_that_up.john_xenakis.my_notie.db.FoldersDAO

import com.code_that_up.john_xenakis.my_notie.db.NotesDAO
import com.code_that_up.john_xenakis.my_notie.db.NotesDB.Companion.getInstance
import com.code_that_up.john_xenakis.my_notie.db.NotesFoldersJoinDAO
import com.code_that_up.john_xenakis.my_notie.model.Folder
import com.code_that_up.john_xenakis.my_notie.model.Note
import com.code_that_up.john_xenakis.my_notie.model.NoteFolderJoin
import com.code_that_up.john_xenakis.my_notie.utils.NoteUtils
import com.code_that_up.john_xenakis.my_notie.utils.OtherUtils
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import java.util.Date
import java.util.Objects

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
 * <h2>EditNoteActivity</h2> is the Activity for editing a new/existing note.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see [https://github.com/ioannis-xenakis/MyNotie](https://github.com/ioannis-xenakis/MyNotie) This project, is uploaded at Github. Visit it if you want!
 */
class EditNoteActivity : AppCompatActivity() {
    /**
     * The notes title editable text.
     */
    private var noteTitle: TextInputEditText? = null

    /**
     * The notes body main editable text.
     */
    private var noteBodyText: TextInputEditText? = null

    /**
     * The note with its content.
     */
    private var note: Note? = null

    /**
     * The notes DAO(Data Object Access) which is responsible for manipulating data, from database.
     */
    private var notesDAO: NotesDAO? = null

    /**
     * The folders DAO(Data Object Access) for accessing folder objects(folders), from database.
     */
    private var foldersDAO: FoldersDAO? = null

    /**
     * The notesFoldersJoin DAO(Data Object Access) which is responsible for accessing notesFoldersJoin object, from database.
     */
    private var notesFoldersJoinDAO: NotesFoldersJoinDAO? = null

    /**
     * The folder list with only checked folders.
     */
    private var checkedFolderList: ArrayList<Folder>? = null

    /**
     * The folder list with only unchecked folders.
     */
    private var unCheckedFolderList: ArrayList<Folder>? = null

    /**
     * The newly checked folder list that contains the folders that are newly checked.
     */
    private var newCheckedFolderList: ArrayList<Folder>? = ArrayList()

    /**
     * The class for determining if folders have any changes on being checked/unchecked.
     */
    private var isCheckedFoldersChanged: Boolean = false

    /**
     * The note title text when first appears and gets created, when this Activity loads.
     */
    private var oldNoteTitle: String? = null

    /**
     * The note body text(notes main text) when first appears and gets created, when this Activity loads.
     */
    private var oldNoteBodyText: String? = null

    /**
     * The boolean state(yes/no, true/false) of, if the note is new.
     */
    private var isItNewNote = false

    /**
     * onCreate gets called/run, when this *Activity*(*EditNoteActivity*) first **loads/starts/gets created**.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        noteTitle = findViewById(R.id.note_title)
        noteBodyText = findViewById(R.id.note_body_text)
        val topAppBar = findViewById<MaterialToolbar>(R.id.top_app_bar_edit_note)
        notesDAO = getInstance(this)!!.notesDAO()
        foldersDAO = getInstance(this)!!.foldersDAO()
        notesFoldersJoinDAO = getInstance(this)!!.notesFoldersJoinDAO()
        if (intent.extras != null) {
            val id = intent.extras!!
                .getInt(NOTE_EXTRA_KEY, 0)
            note = notesDAO!!.getNoteById(id)
            noteTitle!!.setText(note!!.noteTitle)
            noteBodyText!!.setText(note!!.noteBodyText)
            isItNewNote = false
        } else {
            isItNewNote = true
            note = Note()
            NoteUtils.increaseNoteIdByOne(notesDAO!!, note!!)
        }
        oldNoteTitle = Objects.requireNonNull(noteTitle!!.text).toString()
        oldNoteBodyText = Objects.requireNonNull(noteBodyText!!.text).toString()
        checkedFolderList = ArrayList(notesFoldersJoinDAO!!.getFoldersFromNote(note!!.id))
        unCheckedFolderList = ArrayList()
        newCheckedFolderList = ArrayList()

        //Left arrow/Go back icon, on Top App Bar.
        topAppBar.setNavigationOnClickListener { finish() }
    }

    /**
     * saveNote, saves the note, the user has typed.
     */
    private fun saveNote() {
        val noteTitleString = Objects.requireNonNull(noteTitle!!.text).toString()
        val noteBodyTextString = Objects.requireNonNull(
            noteBodyText!!.text
        ).toString()
        val date = Date().time
        note!!.noteTitle = noteTitleString
        note!!.noteBodyText = noteBodyTextString
        note!!.noteDate = date
        if (noteTitleString == "" && noteBodyTextString == "") {
            notesDAO!!.deleteNote(note!!)
        } else if (oldNoteTitle == noteTitleString && oldNoteBodyText == noteBodyTextString) {
            saveNoteToFolders()
            finishNoSave()
        } else {
            saveNewNoteOrUpdateNote()
            saveNoteToFolders()
        }
    }

    /**
     * saveNewNoteOrUpdateNote saves a note if it is new, or updates a note if it exists.
     */
    private fun saveNewNoteOrUpdateNote() {
        if (isItNewNote) {
            try {
                notesDAO!!.insertNote(note!!)
                Toast.makeText(applicationContext, "New note saved!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(applicationContext, "Saving new note failed!", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            try {
                notesDAO!!.updateNote(note!!)
                Toast.makeText(applicationContext, "Note saved!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(applicationContext, "Saving note failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Saves note to checked folders.
     */
    private fun saveNoteToFolders() {
        if (isCheckedFoldersChanged) {
            for (unCheckedFolder in unCheckedFolderList!!) {
                val noteFolderJoin = NoteFolderJoin(note!!.id, unCheckedFolder.id)
                notesFoldersJoinDAO!!.deleteNoteNoteFolderJoin(noteFolderJoin)
                Log.d(TAG, "EditNote saveToFolders deleteUnCheckedFolder: ${unCheckedFolder.folderName}")
            }

            for (checkedFolder in newCheckedFolderList!!) {
                val noteFolderJoin = NoteFolderJoin(note!!.id, checkedFolder.id)
                notesFoldersJoinDAO!!.insertNoteFolderJoin(noteFolderJoin)
                Log.d(TAG, "EditNote saveToFolders insertCheckedFolder: ${checkedFolder.folderName}")
            }
        }
    }

    /**
     * finish gets called/runs, when this Activity gets closed/changes to another activity.
     */
    override fun finish() {
        saveNote()
        OtherUtils.closeKeyboard(this)
        super.finish()
    }

    /**
     * finishNoSave gets called/runs, when this Activity gets closed,
     * without saving the note, and just exits from this Activity.
     */
    private fun finishNoSave() {
        super.finish()
    }

    /**
     * onDeleteNoteClick gets called/runs,
     * when the delete note button is clicked from the user.
     * @param menuItem The *Delete note* button.
     */
    fun onDeleteNoteClick(@Suppress("UNUSED_PARAMETER") menuItem: MenuItem?) {
        notesDAO!!.deleteNote(note!!)
        finishNoSave()
        Toast.makeText(applicationContext, "Note deleted!", Toast.LENGTH_SHORT).show()
    }

    /**
     * onRevertChangesClick gets called/runs,
     * when *Revert changes* button is clicked from the user.
     * @param menuItem The *Revert changes* button.
     */
    fun onRevertChangesClick(@Suppress("UNUSED_PARAMETER") menuItem: MenuItem?) {
        noteTitle!!.setText(oldNoteTitle)
        noteBodyText!!.setText(oldNoteBodyText)
        Toast.makeText(applicationContext, "Changes Reverted!", Toast.LENGTH_SHORT).show()
    }

    /**
     * onAddToFoldersButtonClick gets called/runs,
     * when *Add to folders* button is clicked from the user.
     * @param menuItem The *Add to folders* button.
     */
    fun onAddToFoldersButtonClick(@Suppress("UNUSED_PARAMETER") menuItem: MenuItem?) {
        if (note != null) {
            val addToFolder = Intent(this, AddToFoldersActivity::class.java)
            Log.d(TAG, "AddToFoldersButtonClick, note id: " + note!!.id)
            addToFolder.putExtra(AddToFoldersActivity.NOTE_EXTRA_KEY, note!!.id)
            addToFolder.putExtra(AddToFoldersActivity.NOTE_KEY, note)
            addToFolder.putParcelableArrayListExtra(AddToFoldersActivity.FOLDER_LIST_KEY, checkedFolderList)
            addToFolder.putParcelableArrayListExtra(AddToFoldersActivity.UNCHECKED_FOLDERS_KEY, unCheckedFolderList)
            addToFolder.putParcelableArrayListExtra(AddToFoldersActivity.NEW_CHECKED_FOLDERS_KEY, newCheckedFolderList)
            addToFolder.putExtra(AddToFoldersActivity.IS_CHECKED_FOLDERS_CHANGED_KEY, isCheckedFoldersChanged)
            resultLauncher.launch(addToFolder)
        } else {
            Log.d(TAG, "Note is null(doesn't exist).")
            Toast.makeText(applicationContext, "Adding to folders failed!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * The result launcher for managing/launching other activities,
     * together with managing/setting results for returning from another activity, values.
     */
    @Suppress("DEPRECATION")
    private var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            checkedFolderList = result.data?.extras?.getParcelableArrayList(AddToFoldersActivity.FOLDER_LIST_KEY)
            unCheckedFolderList = result.data?.extras?.getParcelableArrayList(AddToFoldersActivity.UNCHECKED_FOLDERS_KEY)
            newCheckedFolderList = result.data?.extras?.getParcelableArrayList(AddToFoldersActivity.NEW_CHECKED_FOLDERS_KEY)
            isCheckedFoldersChanged = result
                .data
                ?.extras
                ?.getBoolean(AddToFoldersActivity.IS_CHECKED_FOLDERS_CHANGED_KEY)!!
        }
    }

    companion object {
        /**
         * The notes key which is used from other activities to use a specific note.
         * In other words, it is used as a key for specifying a note.
         */
        const val NOTE_EXTRA_KEY = "note_id"

        /**
         * The tag text with the apps name. Usually used for logging messages.
         */
        const val TAG = "MyNotie"
    }
}