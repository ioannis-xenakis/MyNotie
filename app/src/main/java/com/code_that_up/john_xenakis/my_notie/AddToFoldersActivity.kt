package com.code_that_up.john_xenakis.my_notie

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.code_that_up.john_xenakis.my_notie.adapters.FoldersAddToFoldersAdapter
import com.code_that_up.john_xenakis.my_notie.db.FoldersDAO
import com.code_that_up.john_xenakis.my_notie.db.NotesDB
import com.code_that_up.john_xenakis.my_notie.model.Folder
import com.code_that_up.john_xenakis.my_notie.model.Note
import com.code_that_up.john_xenakis.my_notie.utils.FolderUtils
import com.code_that_up.john_xenakis.my_notie.utils.OtherUtils
import com.code_that_up.john_xenakis.my_notie.utils.SpacesItemGrid
import com.google.android.material.appbar.MaterialToolbar
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
 * <h2>AddToFoldersActivity</h2> is the Activity for adding notes to folders.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see [https://github.com/ioannis-xenakis/MyNotie](https://github.com/ioannis-xenakis/MyNotie) This project, is uploaded at Github. Visit it if you want!
 */
class AddToFoldersActivity : AppCompatActivity() {
    /**
     * Folders list displays all the existing folders, to the screen.
     */
    private var foldersListRv: RecyclerView? = null

    /**
     * The dao needed(Data Access Object), for managing folders, in database.
     */
    private var folderDao: FoldersDAO? = null

    /**
     * Adapter for folders, which works as an exchange between user interface and data.
     * It manages folders list(RecyclerView) and each folder.
     */
    private var foldersAdapter: FoldersAddToFoldersAdapter? = null

    /**
     * The note that will be added to folders.
     */
    private var note: Note? = null

    /**
     * onCreate gets called/run, when this *Activity*(*AddToFoldersActivity*) first **loads/starts/gets created**.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_folders)
        val topAppBar = findViewById<MaterialToolbar>(R.id.top_app_bar_add_to_folders)
        val addNewFolderButton = findViewById<ImageButton>(R.id.add_new_folder_button)
        val nameNewFolderText =
            findViewById<TextInputEditText>(R.id.name_new_folder_text_input_edittext)
        val rejectNewFolderButton = findViewById<ImageButton>(R.id.reject_new_folder_button)
        val acceptNewFolderButton = findViewById<ImageButton>(R.id.accept_new_folder_button)
        foldersListRv = findViewById(R.id.folder_list_add_to_folders)
        folderDao = NotesDB.getInstance(this)!!.foldersDAO()
        val orientation = resources.configuration.orientation
        val smallestScreenWidth = resources.configuration.smallestScreenWidthDp
        val notesDAO = NotesDB.getInstance(this)!!.notesDAO()

        //Get the note from "My Notes Activity", to be added to folders.
        if (intent.extras != null) {
            val id = intent.extras!!
                .getInt(NOTE_EXTRA_KEY, 0)
            note = notesDAO!!.getNoteById(id)
        } else {
            Log.e("MyNotie", "Note id is not found/is null.")
        }

        //For different screen orientations(Portrait or Landscape). Mobile phones only and smaller device screen sizes.
        if (orientation == Configuration.ORIENTATION_PORTRAIT && smallestScreenWidth < 600) {
            folderSpacing(1)
            foldersListRv!!.layoutManager = LinearLayoutManager(this)
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE && smallestScreenWidth < 600) {
            folderSpacing(2)
            foldersListRv!!.layoutManager = GridLayoutManager(this, 2)
        }

        //For different screen orientations(Portrait or Landscape). Mostly for tablets and bigger device screen sizes.
        if (orientation == Configuration.ORIENTATION_PORTRAIT && smallestScreenWidth >= 600) {
            folderSpacing(2)
            foldersListRv!!.layoutManager = GridLayoutManager(this, 2)
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE && smallestScreenWidth >= 600) {
            folderSpacing(3)
            foldersListRv!!.layoutManager = GridLayoutManager(this, 3)
        }
        loadFolders()

        //Functionality for "Add new folder button" when clicked on it.
        addNewFolderButton.setOnClickListener {
            nameNewFolderText.requestFocus()
            OtherUtils.openKeyboard(this)
        }

        acceptNewFolderButton.setOnClickListener {
            saveNewFolder(
                nameNewFolderText
            )
        }

        /*
        Functionality for the Done key, in keyboard, when keyboard is opened by user,
        for typing on <i>Name new folder text</i> Edittext.
         */
        nameNewFolderText.setOnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveNewFolder(nameNewFolderText)
                return@setOnEditorActionListener true
            }
            false
        }

        //Functionality for "Reject new folder button" when clicked on it.
        rejectNewFolderButton.setOnClickListener {
            nameNewFolderText.setText("")
            nameNewFolderText.clearFocus()
        }

        /*
        Functionality for showing "Accept new folder" and "Reject new folder" buttons,
        when user types in "Name new folder text" Edittext
        and hides these buttons if "Name new folder text" Edittext doesn't have text in it.
         */
        nameNewFolderText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.toString().trim { it <= ' ' } == "") {
                    hideAcceptRejectNewFolderButtons(
                        addNewFolderButton,
                        rejectNewFolderButton,
                        acceptNewFolderButton
                    )
                } else {
                    showAcceptRejectNewFolderButtons(
                        addNewFolderButton,
                        rejectNewFolderButton,
                        acceptNewFolderButton
                    )
                }
            }
        })

        /*
        Clicking functionality for Navigation button, on "Top app bar".
        If user clicks on Navigation button, he goes back to the previous Activity,
        the user were before this Activity.
        */
        topAppBar.setNavigationOnClickListener { finish() }
    }

    /**
     * saveNewFolder, saves and adds the new folder, to the database and folders list.
     * @param name_new_folder_text The Material Edittext "Name new folder text" for user to type his new folder name.
     */
    private fun saveNewFolder(name_new_folder_text: TextInputEditText) {
        try {
            OtherUtils.closeKeyboard(this)
            val folder = Folder()
            FolderUtils.increaseFolderIdByOne(folderDao!!, folder)
            folder.folderName = Objects.requireNonNull(name_new_folder_text.text).toString()
            folderDao!!.insertFolder(folder)
            foldersAdapter!!.addFolder(folder)
            foldersAdapter!!.notifyItemInserted(foldersAdapter!!.getFolderPosition(folder))
            name_new_folder_text.setText("")
            name_new_folder_text.clearFocus()
            Toast.makeText(applicationContext, "New folder created!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Creating new folder failed!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * folderSpacing is for spacing the folders, inside folders list(RecyclerView).
     * @param spanCount The number of columns.
     */
    private fun folderSpacing(spanCount: Int) {
        val folderSpacingInPixels =
            resources.getDimensionPixelSize(R.dimen.folder_horizontal_space_add_to_folders)
        foldersListRv!!.addItemDecoration(SpacesItemGrid(folderSpacingInPixels, spanCount))
    }

    /**
     * Loads/displays/refreshes all folders,
     * in *folders list*.
     */
    private fun loadFolders() {
        val notesFoldersJoinDao = NotesDB.getInstance(this)!!.notesFoldersJoinDAO()
        val folderList = folderDao!!.allFolders
        val checkedFolderList = notesFoldersJoinDao!!.getFoldersFromNote(
            note!!.id
        )
        val folders = ArrayList(folderList!!)
        checkAlreadyCheckedFolders(checkedFolderList, folderList)
        foldersAdapter = FoldersAddToFoldersAdapter(
            folders,
            checkedFolderList?.toMutableList(),
            note!!,
            this,
            folderDao!!,
            notesFoldersJoinDao
        )
        foldersListRv!!.adapter = foldersAdapter
    }

    /**
     * Checks the already checked state of checkbox in a folder and loads all checked folders.
     * @param checkedFolderList The folder list that contains the checked folders.
     * @param folderList The list of all folders that exist.
     */
    private fun checkAlreadyCheckedFolders(
        checkedFolderList: List<Folder?>?,
        folderList: List<Folder?>?
    ) {
        if (checkedFolderList != null && folderList != null) {
            for (checkedFolder in checkedFolderList) {
                for (folder in folderList) {
                    if (checkedFolder!!.id == folder!!.id) {
                        folder.checked = true
                    }
                }
            }
        }
    }

    /**
     * hideAcceptRejectNewFolderButtons, hides the "Accept new folder" and "Reject new folder" buttons,
     * and hides the "Add new folder button".
     * @param add_new_folder_button The "Add new folder" button.
     * @param reject_new_folder_button The "Reject new folder" button.
     * @param accept_new_folder_button The "Accept new folder" button.
     */
    private fun hideAcceptRejectNewFolderButtons(
        add_new_folder_button: ImageButton,
        reject_new_folder_button: ImageButton,
        accept_new_folder_button: ImageButton
    ) {
        add_new_folder_button.visibility = View.VISIBLE
        reject_new_folder_button.visibility = View.GONE
        accept_new_folder_button.visibility = View.GONE
    }

    /**
     * showAcceptRejectNewFolderButtons, shows the "Accept new folder" and "Reject new folder" buttons
     * and hides the "Add new folder" button.
     * @param add_new_folder_button The "Add new folder" button.
     * @param reject_new_folder_button The "Reject new folder" button.
     * @param accept_new_folder_button The "Accept new folder" button.
     */
    private fun showAcceptRejectNewFolderButtons(
        add_new_folder_button: ImageButton,
        reject_new_folder_button: ImageButton,
        accept_new_folder_button: ImageButton
    ) {
        add_new_folder_button.visibility = View.GONE
        reject_new_folder_button.visibility = View.VISIBLE
        accept_new_folder_button.visibility = View.VISIBLE
    }

    companion object {
        /**
         * The id number of the note, that *identifies* the note, to be added to folders.
         */
        const val NOTE_EXTRA_KEY = "note_id"
    }
}