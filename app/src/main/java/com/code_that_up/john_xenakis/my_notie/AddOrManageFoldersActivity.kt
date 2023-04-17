package com.code_that_up.john_xenakis.my_notie

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.code_that_up.john_xenakis.my_notie.adapters.FoldersManageFoldersAdapter
import com.code_that_up.john_xenakis.my_notie.db.FoldersDAO
import com.code_that_up.john_xenakis.my_notie.db.NotesDB.Companion.getInstance
import com.code_that_up.john_xenakis.my_notie.model.Folder
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
 * <h2>AddOrManageFoldersActivity</h2> is the Activity for *adding* or *managing* folders.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see [https://github.com/ioannis-xenakis/MyNotie](https://github.com/ioannis-xenakis/MyNotie) This project, is uploaded at Github. Visit it if you want!
 */
class AddOrManageFoldersActivity : AppCompatActivity() {
    /**
     * The dao needed, for managing folders, in database.
     */
    private var folderDao: FoldersDAO? = null

    /**
     * foldersList displays all the existing folders
     */
    private var foldersListRV: RecyclerView? = null

    /**
     * The layoutManager **lays out** and manages all folders in a grid/order, in *folders list* for *Add Or Manage Folders* page.
     */
    private var layoutManager: LinearLayoutManager? = null

    /**
     * Adapter for folders, which works as an exchange between user interface and data.
     * It manages folders list and each folder.
     */
    private var foldersAdapter: FoldersManageFoldersAdapter? = null

    /**
     * onCreate gets called/run, when this *Activity*(*AddOrManageFoldersActivity*) first **loads/starts/gets created**.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_manage_folders)
        val topAppBar = findViewById<MaterialToolbar>(R.id.top_app_bar_add_or_manage_folders)
        val nameNewFolderText =
            findViewById<TextInputEditText>(R.id.name_new_folder_text_input_edittext)
        val addNewFolderButton = findViewById<ImageButton>(R.id.add_new_folder_button)
        val rejectNewFolderButton = findViewById<ImageButton>(R.id.reject_new_folder_button)
        val acceptNewFolderButton = findViewById<ImageButton>(R.id.accept_new_folder_button)
        foldersListRV = findViewById(R.id.folder_list_add_or_manage_folders)
        folderDao = getInstance(this)!!.foldersDAO()
        val orientation = resources.configuration.orientation
        val smallestScreenWidth = resources.configuration.smallestScreenWidthDp

        //For different screen orientations(Portrait or Landscape). Mobile phones only and smaller device screen sizes.
        if (orientation == Configuration.ORIENTATION_PORTRAIT && smallestScreenWidth < 600) {
            layoutManager = LinearLayoutManager(this)
            folderSpacing(1)
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE && smallestScreenWidth < 600) {
            layoutManager = GridLayoutManager(this, 2)
            folderSpacing(2)
        }

        //For different screen orientations(Portrait or Landscape). Mostly for tablets and bigger device screen sizes.
        if (orientation == Configuration.ORIENTATION_PORTRAIT && smallestScreenWidth >= 600) {
            layoutManager = GridLayoutManager(this, 2)
            folderSpacing(2)
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE && smallestScreenWidth >= 600) {
            layoutManager = GridLayoutManager(this, 3)
            folderSpacing(3)
        }
        foldersListRV!!.layoutManager = layoutManager
        loadFolders()

        /*
        Functionality for the Done key in keyboard,
         when keyboard is opened by user, for typing on "Name new folder text" Edittext.
         */
        nameNewFolderText.setOnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveNewFolder(nameNewFolderText)
                return@setOnEditorActionListener true
            }
            false
        }

        /*
        Functionality for showing "Accept new folder" and "Reject new folder" buttons,
        when user types in "Add new folder" text field
        and hides these buttons, if "Add new folder" text field doesn't have text in it.
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

        //Functionality for "Add new folder button" when clicked on it.
        addNewFolderButton.setOnClickListener {
            nameNewFolderText.requestFocus()
            OtherUtils.openKeyboard(this)
        }

        //Functionality for "Accept new folder button" when clicked on it.
        acceptNewFolderButton.setOnClickListener {
            saveNewFolder(
                nameNewFolderText
            )
        }

        //Functionality for "Reject new folder button" when clicked on it.
        rejectNewFolderButton.setOnClickListener {
            nameNewFolderText.setText("")
            nameNewFolderText.clearFocus()
        }

        //Clicking functionality for Navigation button, on "Top app bar".
        topAppBar.setNavigationOnClickListener { finish() }
    }

    /**
     * onResume, runs/is called, when resuming the app(My Notie),
     * while android device is on sleep, or opening/switching to the app.
     */
    override fun onResume() {
        super.onResume()
        loadFolders()
    }

    /**
     * Loads/displays/refreshes all existing folders,
     * in *folders list*.
     */
    private fun loadFolders() {
        val folderList = folderDao!!.allFolders
        val folders = ArrayList(folderList!!)
        foldersAdapter = FoldersManageFoldersAdapter(folders, this, folderDao!!)
        foldersListRV!!.adapter = foldersAdapter
    }

    /**
     * saveNewFolder, saves and adds the new folder to database and folder list.
     * @param name_new_folder_text The Material Edittext "Name new folder text", for user to type his new folder name.
     */
    private fun saveNewFolder(name_new_folder_text: TextInputEditText) {
        try {
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
            resources.getDimensionPixelSize(R.dimen.folder_horizontal_space_manage_folders)
        foldersListRV!!.addItemDecoration(SpacesItemGrid(folderSpacingInPixels, spanCount))
    }

    /**
     * hideAcceptRejectNewFolderButtons, hides the "Accept new folder" and "Reject new folder" buttons,
     * and shows the "Add new folder" button.
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
     * showAcceptRejectNewFolderButtons, shows the "Accept new folder" and "Reject new folder" buttons,
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
}