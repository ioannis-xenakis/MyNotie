package com.code_that_up.john_xenakis.my_notie.adapters

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.code_that_up.john_xenakis.my_notie.R
import com.code_that_up.john_xenakis.my_notie.db.FoldersDAO
import com.code_that_up.john_xenakis.my_notie.db.NotesFoldersJoinDAO
import com.code_that_up.john_xenakis.my_notie.model.Folder
import com.code_that_up.john_xenakis.my_notie.model.Note
import com.code_that_up.john_xenakis.my_notie.model.NoteFolderJoin
import com.code_that_up.john_xenakis.my_notie.utils.OtherUtils
import com.google.android.material.textfield.TextInputEditText
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
 * <h2>FoldersAddToFoldersAdapter</h2> is the adapter and the *middle man* between the folders ui
 * and the actual data to be set to folders, in *Folders list(RecyclerView)*, from *Add To Folders Activity*.
 * *FoldersAddToFoldersAdapter*, is responsible for a RecyclerView(*Folders list*) and managing its data, in each folder.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see [https://github.com/ioannis-xenakis/MyNotie](https://github.com/ioannis-xenakis/MyNotie) This project, is uploaded at Github. Visit it if you want!
 */
class FoldersAddToFoldersAdapter
/**
 * The constructor needed, for initializing/creating this *FoldersAddToFoldersAdapter* class.
 * @param folders The folders list, displayed on screen.
 * @param context The *context/current state* of an Activity(*AddToFoldersActivity*), that is associated with this RecyclerView Adapter.
 */(
    /**
     * The folders list, which is displayed at screen.
     */
    private val folders: ArrayList<Folder?>,
    /**
     * The checked folders of the folders list.
     */
    private val checkedFolders: MutableList<Folder?>?,
    /**
     * The note to be added to folders.
     */
    private val note: Note,
    /**
     * The current state of the Activity(*AddToFoldersActivity*) that is associated with this RecyclerView Adapter.
     */
    private val context: Context,
    /**
     * Dao(Data Access Object) needed for managing folders, in database.
     */
    private val foldersDao: FoldersDAO,
    /**
     * Dao(Data Access Object) needed for managing the links between a note and a folder, in database.
     */
    private val notesFoldersJoinDao: NotesFoldersJoinDAO
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * OnCreateViewHolder gets called/runs, when a folder is *created*.
     * @param parent The folder/group that nests the buttons/content of the folder.
     * @param viewType The type of a folder.
     * @return A new folder holder that holds content/buttons in a folder. A new folder with it's content/buttons(views).
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.folder_add_to_folders_layout, parent, false)
        return FolderHolder(v, this, notesFoldersJoinDao)
    }

    /**
     * onBindViewHolder, binds the needed data or make actions to each button/element(view), on each folder
     * and displays each folder to the proper position.
     * @param holder The holder which holds all content/buttons(called views), in each folder.
     * @param position The position of each folder, in folders list.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val folderHolder = holder as FolderHolder
        val folder = getFolder(position)
        folderHolder.folderName.setText(folder.folderName)

        //Load up the already checked folders when "Add To Folders Activity" loads up and check them.
        folderHolder.setData(note, folder)
        folderHolder.checkBoxAddToFolders.isChecked = folder.checked
    }

    /**
     * getFolder, gets a *specific* folder, from a *specific* position, in folders list.
     * @param position The *position*(integer) the folder is located in folders list.
     * @return The folder.
     */
    private fun getFolder(position: Int): Folder {
        return folders[position]!!
    }

    /**
     * Adds a folder, in folder list.
     * @param folder The folder to be added.
     */
    fun addFolder(folder: Folder) {
        folders.add(folder)
    }

    /**
     * Gets folder position for a *specific* folder, in *folders list*.
     * @param folder The folder for getting its position.
     * @return The folders position.
     */
    fun getFolderPosition(folder: Folder): Int {
        return folders.indexOf(folder)
    }

    /**
     * Adds the folder to be checked to checked folders list.
     * @param folder The folder to be checked.
     */
    fun addCheckedFolder(folder: Folder?) {
        checkedFolders?.add(folder)
    }

    /**
     * Removes the checked folder to be unchecked, from checked folders list.
     * @param folder The checked folder.
     */
    fun removeCheckedFolder(folder: Folder?) {
        checkedFolders?.remove(folder)
    }

    /**
     * Gets and returns the sum of all folders or how many the folders are, in the database/folders list,
     * displayed in *folders list* RecyclerView.
     * @return The sum number of all the folders in folders list.
     */
    override fun getItemCount(): Int {
        return folders.size
    }

    /**
     * FolderHolder, that holds all content/buttons(views), in a folder.
     */
    class FolderHolder(
        itemView: View,
        adapter: FoldersAddToFoldersAdapter,
        notesFoldersJoinDao: NotesFoldersJoinDAO
    ) : RecyclerView.ViewHolder(itemView) {
        /**
         * The folder name that user typed;
         */
        var folderName: TextInputEditText

        /**
         * Checkbox adds/removes the note from the folder.
         */
        var checkBoxAddToFolders: CheckBox

        /**
         * CancelRenamingFolder button, cancels renaming folder and reverts back the folder name to the previous name.
         */
        var cancelRenamingFolder: ImageButton

        /**
         * AcceptRenamingFolder button, accepts and saves the folder.
         */
        var acceptRenamingFolder: ImageButton

        /**
         * The adapter for the recyclerview(folders), in AddToFoldersActivity.
         */
        var adapter: FoldersAddToFoldersAdapter

        /**
         * The note to be added in the folders.
         */
        var note: Note? = null

        /**
         * The folder that is in folders list(RecyclerView), from AddToFoldersActivity.
         */
        var folder: Folder? = null

        /**
         * The Data Object Access("Dao" for short) for having a connection between a note and a folder
         * and manipulating them(adding/deleting/updating/querying) in the database.
         */
        var notesFoldersJoinDao: NotesFoldersJoinDAO

        /**
         * The constructor needed, for initializing/creating this *FolderHolder* class.
         */
        init {
            checkBoxAddToFolders = itemView.findViewById(R.id.checkbox_add_to_folders)
            cancelRenamingFolder = itemView.findViewById(R.id.cancel_renaming_folder_button)
            acceptRenamingFolder = itemView.findViewById(R.id.accept_renaming_folder_button)
            folderName = itemView.findViewById(R.id.folder_name_edittext)
            this.notesFoldersJoinDao = notesFoldersJoinDao
            this.adapter = adapter

            /*
            Managing check states of "checkBox Add To Folders" and what should do when it is checked or not.
            Add note to folder when checkBox is checked.
            Remove note from folder when checkBox is unchecked.
             */checkBoxAddToFolders.setOnClickListener {
                val isChecked = checkBoxAddToFolders.isChecked
                val noteFolderJoin = NoteFolderJoin(note!!.id, folder!!.id)
                if (isChecked) {
                    folder!!.checked = true
                    adapter.addCheckedFolder(folder)
                    notesFoldersJoinDao.insertNoteFolderJoin(noteFolderJoin)
                } else {
                    folder!!.checked = false
                    adapter.removeCheckedFolder(folder)
                    notesFoldersJoinDao.deleteNoteNoteFolderJoin(noteFolderJoin)
                }
            }

            /*
            Functionality when focusing on "Folder name" Edittext,
            showing "Accept renaming folder" and "Cancel renaming folder" buttons
            and hiding them when unfocused.
             */folderName.onFocusChangeListener =
                OnFocusChangeListener { _: View?, hasFocus: Boolean ->
                    showAcceptCancelButtons()
                    if (!hasFocus) {
                        hideAcceptCancelButtons()
                    }
                }

            /*
            Functionality for the Done key in keyboard, when keyboard is opened by user,
            for typing on "Folder name" Edittext.
            When Done key is pressed, it saves the folder with another name that the user named.
             */folderName.setOnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveRenamedFolder(folder)
                    return@setOnEditorActionListener true
                }
                false
            }

            //When "Accept renaming folder" button is clicked, it saves the folder with another name that the user gave.
            acceptRenamingFolder.setOnClickListener { saveRenamedFolder(folder) }

            /*
            When "Cancel renaming folder" button is clicked, it cancels the already renamed folder
            that the user renamed with "Folder name" Edittext
            and returns to the previous existing folder name.
             */cancelRenamingFolder.setOnClickListener {
                folderName.setText(folder!!.folderName)
                folderName.clearFocus()
                OtherUtils.closeKeyboard(adapter.context, folderName)
            }
        }

        /**
         * Sets the needed data/objects for the *FolderHolder* class(this class) to
         * @param note The note to be added to folders.
         * @param folder The folder to add the note to.
         */
        fun setData(note: Note?, folder: Folder?) {
            this.note = note
            this.folder = folder
        }

        /**
         * Shows *Accept renaming folder* and *Cancel renaming folder* buttons
         * and hides *Checkbox add to folders*.
         */
        private fun showAcceptCancelButtons() {
            acceptRenamingFolder.visibility = View.VISIBLE
            cancelRenamingFolder.visibility = View.VISIBLE
            checkBoxAddToFolders.visibility = View.GONE
        }

        /**
         * Hides *Accept renaming folder* and *Cancel renaming folder* buttons
         * and shows *Checkbox add to folders*.
         */
        private fun hideAcceptCancelButtons() {
            acceptRenamingFolder.visibility = View.GONE
            cancelRenamingFolder.visibility = View.GONE
            checkBoxAddToFolders.visibility = View.VISIBLE
        }

        /**
         * saveRenamedFolder, saves the renamed folder,
         * renamed by the "Folder name" edittext,
         * the user used to rename the folder.
         * @param folder The folder to be renamed.
         */
        private fun saveRenamedFolder(folder: Folder?) {
            folder!!.folderName = Objects.requireNonNull(folderName.text).toString()
            adapter.foldersDao.updateFolder(folder)
            folderName.clearFocus()
            OtherUtils.closeKeyboard(adapter.context, folderName)
        }
    }
}