package com.code_that_up.john_xenakis.my_notie.adapters

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.code_that_up.john_xenakis.my_notie.R
import com.code_that_up.john_xenakis.my_notie.db.FoldersDAO
import com.code_that_up.john_xenakis.my_notie.model.Folder
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
 * <h2>FoldersManageFoldersAdapter</h2> is the adapter and the *middle man* between the folders ui
 * and the actual data to be set to folders, in folders list, from *Add Or Manage Folders Activity*.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see [https://github.com/ioannis-xenakis/MyNotie](https://github.com/ioannis-xenakis/MyNotie) This project, is uploaded at Github. Visit it if you want!
 */
class FoldersManageFoldersAdapter(
    /**
     * The folders list, which is displayed at screen.
     */
    private val folders: ArrayList<Folder?>,
    /**
     * The current state and **context** of the folders.
     */
    private val context: Context,
    /**
     * Dao needed, for managing folders, in database.
     */
    private val foldersDao: FoldersDAO
) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    /**
     * onCreateViewHolder, gets called/runs, when a folder is **created**.
     * @param parent The folder/group that nests the buttons/content of the folder.
     * @param viewType The type of a folder.
     * @return A new folder holder, that holds new content/buttons, in folder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.folder_manage_folders_layout, parent, false)
        return FolderHolder(v)
    }

    /**
     * onBindViewHolder, binds the needed data or make actions to each button/element, on each folder
     * and displays each folder to the proper position.
     * @param holder The holder which holds all content/buttons, in each folder.
     * @param position The position of each folder, in folders list.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val folderHolder = holder as FolderHolder
        val folder = getFolder(position)
        folderHolder.folderName.setText(folder.folderName)

        /*
        Functionality when focusing on "Folder name" Edittext,
        showing "Accept renaming folder" and "Cancel renaming folder" buttons when focused
        and hiding when unfocused.
         */
        folderHolder.folderName.onFocusChangeListener =
        OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            showAcceptCancelButtons(folderHolder)
            if (!hasFocus) {
                hideAcceptCancelButtons(folderHolder)
            }
        }

        /*
        Functionality for the Done key in keyboard, when keyboard is opened by user, for typing on "Folder name" Edittext.
        When Done key is clicked, it saves the folder with another name that the user named.
         */
        folderHolder.folderName.setOnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            saveRenamedFolder(folder, folderHolder)
            return@setOnEditorActionListener true
        }
        false
    }

        //Functionality for the "Delete folder" button, that when clicked, deletes the folder with the button that is associated with.
        folderHolder.deleteFolder.setOnClickListener {
            foldersDao.deleteFolder(folder)
            folders.remove(folder)
            notifyItemRemoved(folderHolder.bindingAdapterPosition)
        }

        //When "Accept renaming folder" button is clicked, it saves the folder with another name that the user named.
        folderHolder.acceptRenamingFolder.setOnClickListener {
            saveRenamedFolder(
                folder,
                folderHolder
            )
        }

        /*
        When "Cancel renaming folder" button is clicked, it cancels the already renamed folder
        that the user renamed with "Folder name" Edittext
        and returns to the previous existing folder name.
         */
        folderHolder.cancelRenamingFolder.setOnClickListener {
            folderHolder.folderName.setText(folder.folderName)
        folderHolder.folderName.clearFocus()
        OtherUtils.closeKeyboard(context, folderHolder.folderName)
    }
    }

    /**
     * getItemCount, gets and **counts** all the existing folders, from folders list.
     * @return The counted folders, from folders list.
     */
    override fun getItemCount(): Int {
        return folders.size
    }

    /**
     * Gets folder position, in folders list.
     * @param folder The folder for getting its position.
     * @return The folder position.
     */
    fun getFolderPosition(folder: Folder): Int {
        return folders.indexOf(folder)
    }

    /**
     * Adds a folder, in folders list.
     * @param folder The folder to be added.
     */
    fun addFolder(folder: Folder) {
        folders.add(folder)
    }

    /**
     * Shows *Accept renaming folder* and *Cancel renaming folder* buttons
     * and hides *Delete folder button*.
     * @param folderHolder The holder which holds all content/buttons, in a folder.
     */
    private fun showAcceptCancelButtons(folderHolder: FolderHolder) {
        folderHolder.acceptRenamingFolder.visibility = View.VISIBLE
        folderHolder.cancelRenamingFolder.visibility = View.VISIBLE
        folderHolder.deleteFolder.visibility = View.GONE
    }

    /**
     * Hides *Accept renaming folder* and *Cancel renaming folder* buttons
     * and shows *Delete folder button*.
     * @param folderHolder The holder which holds all content/buttons, in a folder.
     */
    private fun hideAcceptCancelButtons(folderHolder: FolderHolder) {
        folderHolder.acceptRenamingFolder.visibility = View.GONE
        folderHolder.cancelRenamingFolder.visibility = View.GONE
        folderHolder.deleteFolder.visibility = View.VISIBLE
    }

    /**
     * saveRenamedFolder, saves the renamed folder,
     * renamed by the "Folder name" edittext,
     * the user used to rename the folder.
     * @param folder The folder to be renamed.
     * @param folderHolder The holder of the folder, that holds all the buttons and edittext.
     */
    private fun saveRenamedFolder(folder: Folder, folderHolder: FolderHolder) {
        folder.folderName = Objects.requireNonNull(folderHolder.folderName.text).toString()
        foldersDao.updateFolder(folder)
        folderHolder.folderName.clearFocus()
        OtherUtils.closeKeyboard(context, folderHolder.folderName)
    }

    /**
     * getFolder, gets a **specific** folder, from folder list.
     * @param position The position of the folder, in folders list.
     * @return The folder, depending on its position.
     */
    private fun getFolder(position: Int): Folder {
        return folders[position]!!
    }

    /**
     * FolderHolder class that holds all content/buttons, in a folder.
     */
    class FolderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         * The folder name text, that the user writes.
         */
        var folderName: TextInputEditText

        /**
         * deleteFolder button, deletes the folder.
         */
        var deleteFolder: ImageButton

        /**
         * cancelRenamingFolder button, cancels renaming folder.
         */
        var cancelRenamingFolder: ImageButton

        /**
         * acceptRenamingFolder button, accepts and renames folder.
         */
        var acceptRenamingFolder: ImageButton

        /**
         * The constructor with initializing folder content/buttons, by their ids.
         */
        init {
            folderName = itemView.findViewById(R.id.folder_name_edittext)
            deleteFolder = itemView.findViewById(R.id.delete_folder_button)
            cancelRenamingFolder = itemView.findViewById(R.id.cancel_renaming_folder_button)
            acceptRenamingFolder = itemView.findViewById(R.id.accept_renaming_folder_button)
        }
    }
}