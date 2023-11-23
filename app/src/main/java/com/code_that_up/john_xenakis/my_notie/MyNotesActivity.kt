package com.code_that_up.john_xenakis.my_notie

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.code_that_up.john_xenakis.my_notie.adapters.NotesAdapter
import com.code_that_up.john_xenakis.my_notie.adapters.NotesAdapter.NoteHolder
import com.code_that_up.john_xenakis.my_notie.callbacks.MoreMenuButtonListener
import com.code_that_up.john_xenakis.my_notie.callbacks.NoteEventListener
import com.code_that_up.john_xenakis.my_notie.db.NotesDAO
import com.code_that_up.john_xenakis.my_notie.db.NotesDB
import com.code_that_up.john_xenakis.my_notie.db.NotesFoldersJoinDAO
import com.code_that_up.john_xenakis.my_notie.model.Folder
import com.code_that_up.john_xenakis.my_notie.model.Note
import com.code_that_up.john_xenakis.my_notie.model.NoteFolderJoin
import com.code_that_up.john_xenakis.my_notie.utils.SpacesItemGrid
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

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
 * <h2>MyNotesActivity</h2> is the starting home page when **My Notie** starts
 * and is the page that displays all the existing notes.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see [https://github.com/ioannis-xenakis/MyNotie](https://github.com/ioannis-xenakis/MyNotie) This project, is uploaded at Github. Visit it if you want!
 */
class MyNotesActivity : AppCompatActivity(), NoteEventListener, MoreMenuButtonListener {
    /**
     * Top bar for users, to search notes.
     */
    private var searchTopBar: MaterialToolbar? = null

    /**
     * The default top bar, which only displays the title name, of *My notes* page.
     */
    private var pageTitleTopBar: MaterialToolbar? = null

    /**
     * Top bar for users, to select notes, and act(for ex. delete) on them.
     */
    private var selectNotesTopBar: MaterialToolbar? = null

    /**
     * Dao needed for managing notes, in database.
     */
    private var dao: NotesDAO? = null

    /**
     * Dao needed for accessing notesFoldersJoin, in database.
     */
    private var notesFoldersDAO: NotesFoldersJoinDAO? = null

    /**
     * List containing only checked folders.
     */
    private var checkedFolders: ArrayList<Folder>? = null

    /**
     * List containing only unchecked folders.
     */
    private var unCheckedFolders: ArrayList<Folder>? = null

    /**
     * Adapter for notes, which works as an exchange between the user interface and actual data.
     */
    private var adapter: NotesAdapter? = null

    /**
     * The RecyclerView, or the **notes list** which displays all the existing notes, on **My notes** page.
     */
    private var recyclerView: RecyclerView? = null

    /**
     * The id number of the checked navigation menu item
     * (for ex. a checked folder, or "All notes" menu item).
     */
    private var navMenuItemCheckedId: Int = ALL_NOTES_ID

    /**
     * The "My folders" sub menu of the navigation view,
     * containing the folders as menu items.
     */
    private var myFoldersSubMenu: SubMenu? = null

    /**
     * The layoutManager **lays out** and manages all notes in an grid/order, in *notes list*, for *My notes* page.
     */
    private var layoutManager: LinearLayoutManager? = null

    /**
     * The group/layout for the side navigation drawer.
     */
    private var drawerLayout: DrawerLayout? = null

    /**
     * The side navigation drawer.
     */
    private var navigationView: NavigationView? = null

    /**
     * The boolean state of, if the note in notes list is checked, or not.
     */
    private var isNoteChecked = false

    /**
     * onCreate gets called/run, when MyNotie first **loads/starts/gets created**.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_notes)
        pageTitleTopBar = findViewById(R.id.top_app_bar_my_notes)
        setSupportActionBar(appBar)
        val smallestScreenWidth = resources.configuration.smallestScreenWidthDp

        notesFoldersDAO = NotesDB.getInstance(this)!!.notesFoldersJoinDAO()

        //For different screen orientations(Portrait or Landscape). Mobile phones only and smaller device screen sizes.
        recyclerView = findViewById(R.id.notes_list)
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT && smallestScreenWidth < 600) {
            layoutManager = LinearLayoutManager(this)
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE && smallestScreenWidth < 600) {
            layoutManager = GridLayoutManager(this, 2)
            noteSpacing(recyclerView, 2)
        }

        //For different screen orientations(Portrait or Landscape). Mostly for android tablets and bigger device screen sizes.
        if (orientation == Configuration.ORIENTATION_PORTRAIT && smallestScreenWidth >= 600) {
            layoutManager = GridLayoutManager(this, 2)
            noteSpacing(recyclerView, 2)
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE && smallestScreenWidth >= 600) {
            layoutManager = GridLayoutManager(this, 3)
            noteSpacing(recyclerView, 3)
        }
        recyclerView!!.layoutManager = layoutManager
        dao = NotesDB.getInstance(this)!!.notesDAO()
        loadNotes()

        /*
          Note search functionality for search edittext on search top bar.
          Text changed listener runs when text is changed inside a Edittext, either the user changes it, or from a code functionality.
         */
        val searchEdittext = findViewById<EditText>(R.id.search_edittext)
        searchEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                adapter!!.filter.filter(editable.toString())
            }
        })

        /*
          Close Search Top Bar button(X icon button), in Search Top Bar,
          which closes Search Top Bar.
         */
        selectNotesTopBar = findViewById(R.id.top_app_bar_select_notes)
        searchTopBar = findViewById(R.id.top_app_bar_search)
        searchTopBar!!.setNavigationOnClickListener {
            closeKeyboard()
            searchEdittext.setText("")
            if (adapter!!.getCheckedNotes().isNotEmpty()) {
                showSelectNotesTopBar()
            } else {
                showPageTitleTopBar()
            }
        }

        /*
          Close Select Notes Top Bar button, in Select Notes Top Bar,
          which closes Select Notes Top Bar.
         */
        selectNotesTopBar!!.setNavigationOnClickListener {
            showPageTitleTopBar()
            adapter!!.setAllCheckedNotesFull(false)
            adapter!!.setAllCheckedNotes(false)
            displaySelectedNotesCount()
            searchEdittext.setText("")
        }

        //Add new note button, which adds/creates new note.
        val addNewNoteButton = findViewById<FloatingActionButton>(R.id.add_new_note)
        TooltipCompat.setTooltipText(
            addNewNoteButton,
            this.getString(R.string.tooltip_add_new_note)
        )
        addNewNoteButton.setOnClickListener {
            val editNoteActivityIntent = Intent(applicationContext, EditNoteActivity::class.java)
            startActivity(editNoteActivityIntent)
        }

        //Listens to scrolling at RecyclerView(notes list).
        recyclerView!!.addOnScrollListener(onScrollListener)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        //Opens/Shows and Closes/Hides Side Navigation Drawer.
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            appBar,
            R.string.open_nav_drawer,
            R.string.close_nav_drawer
        )
        drawerLayout!!.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        //Gets the Header from Navigation Drawer(Navigation View).
        val headerView = navigationView!!.getHeaderView(0)
        //Gets the close button, from navigation Drawer.
        val closeButton = headerView.findViewById<MaterialButton>(R.id.close_nav_drawer_button)
        //Listens to clicks on close button.
        closeButton.setOnClickListener {
            //Closes/hides Navigation Drawer from Right/End, to Left/Start.
            drawerLayout!!.closeDrawer(GravityCompat.START)
        }

        //The menu items(buttons) click mechanism for the side navigation drawer(in navigation view).
        navigationView!!.setNavigationItemSelectedListener { menuItem: MenuItem ->
            when (val id = menuItem.itemId) {
                ALL_NOTES_ID -> {
                    onlyRefreshAndLoadAllNotes()
                    displaySelectedNotesCount()
                    pageTitleTopBar!!.setTitle(R.string.page_title)
                    navMenuItemCheckedId = ALL_NOTES_ID
                }
                GO_TO_ADD_OR_MANAGE_FOLDERS_ID -> {
                    val manageFoldersActivityIntent =
                        Intent(applicationContext, AddOrManageFoldersActivity::class.java)
                    startActivity(manageFoldersActivityIntent)
                }
                else -> {
                    val folderList = NotesDB.getInstance(this)!!.foldersDAO()!!.allFolders
                    for (folder in folderList!!) {
                        if (id == folder!!.id) {
                            loadNotesFromFolder(folder)
                            displaySelectedNotesCount()
                            pageTitleTopBar!!.title = folder.folderName!!.trim { it <= ' ' }
                            navMenuItemCheckedId = id
                        }
                    }
                }
            }
            drawerLayout!!.closeDrawer(GravityCompat.START)
            true
        }
        isNoteChecked = false
        inflateNavigationMenus()

        checkedFolders = ArrayList()
    }

    /**
     * inflates(creates), the menu items(buttons) and the menu, in navigation view(side navigation drawer).
     */
    private fun inflateNavigationMenus() {
        val menu = navigationView!!.menu
        menu.clear()
        menu.add(Menu.NONE, ALL_NOTES_ID, Menu.NONE, "All notes")
            .setIcon(R.drawable.note_icon)
            .setCheckable(true).isChecked = true

        val folderList = NotesDB.getInstance(this)!!.foldersDAO()!!.allFolders
        myFoldersSubMenu = menu.addSubMenu("My folders")
        for (folder in folderList!!) {
            myFoldersSubMenu!!.add(Menu.NONE, folder!!.id, Menu.NONE, folder.folderName)
                .setIcon(R.drawable.folder_icon).isCheckable = true
        }

        menu.add(Menu.NONE, GO_TO_ADD_OR_MANAGE_FOLDERS_ID, Menu.NONE, "Add or manage folders")
            .setIcon(R.drawable.new_folder_icon)
    }

    /**
     * getAppBar, gets and returns either the Top Bar, or the Bottom App Bar, depending on the devices screen size.
     * @return the App Bar.
     */
    private val appBar: Toolbar?
        get() {
            val smallestScreenWidth = resources.configuration.smallestScreenWidthDp
            val appBar: Toolbar? = if (smallestScreenWidth < 600) {
                findViewById<BottomAppBar>(R.id.bottom_app_bar)
            } else {
                pageTitleTopBar
            }
            return appBar
        }

    /**
     * onScrollListener, Listens for scrolls when user scrolls, on *notes list*, from *My notes* Activity.
     */
    private val onScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {

            /**
             * onScrolled runs, when user scrolls.
             * onScrolled runs, *after* the scroll has completed.
             * @param recyclerView The *Notes list* RecyclerView.
             * @param dx The amount of horizontal scroll.
             * @param dy The amount of vertical scroll.
             */
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (resources.configuration.smallestScreenWidthDp < 600) {
                    showHideBottomAppBar()
                }
            }
        }

    /**
     * noteSpacing, is for spacing the notes, inside notes list(RecyclerView).
     */
    private fun noteSpacing(recyclerView: RecyclerView?, spanCount: Int) {
        val noteSpacingInPixels =
            resources.getDimensionPixelSize(R.dimen.note_between_horizontal_space)
        recyclerView!!.addItemDecoration(SpacesItemGrid(noteSpacingInPixels, spanCount))
    }

    /**
     * closeKeyboard, closes the user's keyboard, if open.
     */
    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * showSelectNotesTopBar, shows/displays the top bar that allows user, to select notes.
     */
    private fun showSelectNotesTopBar() {
        searchTopBar = findViewById(R.id.top_app_bar_search)
        pageTitleTopBar = findViewById(R.id.top_app_bar_my_notes)
        selectNotesTopBar = findViewById(R.id.top_app_bar_select_notes)
        pageTitleTopBar!!.visibility = View.GONE
        searchTopBar!!.visibility = View.GONE
        selectNotesTopBar!!.visibility = View.VISIBLE
    }

    /**
     * showPageTitleTopBar, shows/displays the default top bar,
     * that only displays the page/Activity title name.
     */
    private fun showPageTitleTopBar() {
        searchTopBar = findViewById(R.id.top_app_bar_search)
        pageTitleTopBar = findViewById(R.id.top_app_bar_my_notes)
        selectNotesTopBar = findViewById(R.id.top_app_bar_select_notes)
        selectNotesTopBar!!.visibility = View.GONE
        searchTopBar!!.visibility = View.GONE
        pageTitleTopBar!!.visibility = View.VISIBLE
    }

    /**
     * showSearchTopBar, shows/displays the top bar, that allows user to search notes.
     */
    private fun showSearchTopBar() {
        searchTopBar = findViewById(R.id.top_app_bar_search)
        pageTitleTopBar = findViewById(R.id.top_app_bar_my_notes)
        selectNotesTopBar = findViewById(R.id.top_app_bar_select_notes)
        selectNotesTopBar!!.visibility = View.GONE
        pageTitleTopBar!!.visibility = View.GONE
        searchTopBar!!.visibility = View.VISIBLE
    }

    /**
     * displaySelectedNotesCount, displays on the top bar when selecting notes, the number of the checked/selected notes,
     * the user has.
     */
    private fun displaySelectedNotesCount() {
        selectNotesTopBar!!.title =
            adapter!!.getCheckedNotes().size.toString() + " notes selected"
    }

    /**
     * selectNote, selects/checks an existing note to act on it(for ex. delete it).
     * @param note the note.
     * @param noteHolder the holder that holds content/buttons in a note.
     */
    private fun selectNote(note: Note?, noteHolder: NoteHolder?) {
        note!!.isChecked = !note.isChecked
        noteHolder!!.noteCardView.isChecked = note.isChecked
        adapter!!.checkOrUncheckNote(note, note.isChecked, 2)
        if (adapter!!.getCheckedNotes().isNotEmpty()) {
            showSelectNotesTopBar()
        } else {
            showPageTitleTopBar()
        }
        displaySelectedNotesCount()
    }

    /**
     * onSelectAllNotesButtonClick, runs/is called when *select all notes* button is clicked,
     * from top bar.
     * *Select all notes* button allows user, to select all existing notes,
     * in *notes list*, in *My notes* Activity.
     * @param menuItem the *select all notes* button.
     */
    fun onSelectAllNotesButtonClick(menuItem: MenuItem?) {
        isNoteChecked = !isNoteChecked
        adapter!!.setAllCheckedNotes(isNoteChecked)
        displaySelectedNotesCount()
    }

    /**
     * onDeleteSelectedNotesButtonClick, runs/is called,
     * when *delete selected notes* button is clicked, from top bar.
     * *Delete selected notes* button allows user to delete all the selected notes,
     * user have selected.
     * @param menuItem the menu item/button(*delete selected notes* button).
     */
    fun onDeleteSelectedNotesButtonClick(menuItem: MenuItem?) {
        val deletedNotes = ArrayList(
            adapter!!.getCheckedNotes()
        )
        for (note in adapter!!.getCheckedNotes()) {
            val position = adapter!!.notes.indexOf(note)
            dao!!.deleteNote(note)
            adapter!!.notes.remove(note)
            adapter!!.notesFull.remove(note)
            adapter!!.notifyItemRemoved(position)
        }
        adapter!!.getCheckedNotes().clear()
        displaySelectedNotesCount()
        Toast.makeText(
            this@MyNotesActivity,
            deletedNotes.size.toString() + " notes deleted!",
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * onSearchButtonClick, runs/is called
     * when *search* button is clicked, from bottom app bar.
     * *Search* button, opens the *search notes top bar*,
     * that allows user to search existing notes.
     * @param menuItem the menu item/button(*search* button).
     */
    fun onSearchButtonClick(menuItem: MenuItem?) {
        showSearchTopBar()
    }

    /**
     * onSelectNotesButtonClick, runs/is called
     * when *select notes* button is clicked, from bottom app bar.
     * *Select notes* button, opens the *select notes top bar*,
     * that allows user to select notes.
     * @param item the menu item/button(*select notes* button).
     */
    fun onSelectNotesButtonClick(item: MenuItem?) {
        showSelectNotesTopBar()
    }

    /**
     * loadNotes, loads/displays/refreshes all the existing notes,
     * in *notes list*.
     */
    private fun loadNotes() {
        val list = dao!!.notes
        val notes = ArrayList(list!!)
        adapter = NotesAdapter(notes, this, this)
        adapter!!.setListener(this, this)
        adapter!!.initCheckedNotes()
        recyclerView!!.adapter = adapter
    }

    /**
     * Only refreshes the notes and not loads the whole adapter. Avoids calling setAdapter.
     */
    private fun onlyRefreshAndLoadAllNotes() {
        adapter!!.setAllCheckedNotes(false)
        val list = dao!!.notes
        adapter!!.updateNoteListAndNotesFull(list!!)
    }

    /**
     * Loads/displays/refreshes notes, only from a specific folder.
     * @param folder The folder to display the notes from.
     */
    private fun loadNotesFromFolder(folder: Folder) {
        adapter!!.setAllCheckedNotes(false)
        val notesFoldersJoinDao = NotesDB.getInstance(this)!!.notesFoldersJoinDAO()
        val noteListFromFolder = notesFoldersJoinDao!!.getNotesFromFolder(folder.id)
        adapter!!.updateNoteListAndNotesFull(noteListFromFolder!!)
    }

    /**
     * Updates the folders in Navigation Drawer(Navigation View).
     * @param folderList The list of folders.
     */
    private fun updateFoldersInNavDrawer(folderList: List<Folder?>?) {
        myFoldersSubMenu!!.clear()

        for (folder in folderList!!) {
            myFoldersSubMenu!!.add(Menu.NONE, folder!!.id, Menu.NONE, folder.folderName)
                .setIcon(R.drawable.folder_icon).isCheckable = true
        }
        navigationView!!.setCheckedItem(navMenuItemCheckedId)
    }

    /**
     * If navigation menu item is already checked, do the proper action.
     * @param folderList The list of folders.
     */
    private fun ifNavMenuItemCheckedDo(folderList: List<Folder?>?) {
        when(val id = navMenuItemCheckedId) {
            ALL_NOTES_ID -> {
                onlyRefreshAndLoadAllNotes()
                pageTitleTopBar!!.setTitle(R.string.page_title)
            }
            else -> {
                for (folder in folderList!!) {
                    if (id == folder!!.id) {
                        loadNotesFromFolder(folder)
                        pageTitleTopBar!!.title = folder.folderName
                    }
                }
            }
        }
    }

    /**
     * showHideBottomAppBar, shows or hides bottom app bar,
     * (enables/disables auto-hide when scrolling on *notes list*),
     * depending on total number of notes on screen.
     */
    private fun showHideBottomAppBar() {
        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottom_app_bar)
        val visibleNotesCount = layoutManager!!.childCount
        val totalNotesCount = layoutManager!!.itemCount
        if (visibleNotesCount < totalNotesCount) {
            bottomAppBar.hideOnScroll = true
        } else {
            bottomAppBar.hideOnScroll = false
            bottomAppBar.performShow()
        }
    }

    /**
     * Saves a note to folders.
     * @param note The note to be saved in folders.
     */
    private fun saveNoteToFolders(note: Note?) {
        for (unCheckedFolder in unCheckedFolders!!) {
            val noteFolderJoin = NoteFolderJoin(note?.id!!, unCheckedFolder.id)
            notesFoldersDAO!!.deleteNoteNoteFolderJoin(noteFolderJoin)
        }

        for (checkedFolder in checkedFolders!!) {
            val noteFolderJoin = NoteFolderJoin(note?.id!!, checkedFolder.id)
            notesFoldersDAO!!.insertNoteFolderJoin(noteFolderJoin)
        }
    }

    /**
     * openNote, opens an existing note, to edit, in EditNoteActivity.java
     */
    private fun openNote(note: Note?) {
        val edit = Intent(this, EditNoteActivity::class.java)
        edit.putExtra(EditNoteActivity.NOTE_EXTRA_KEY, note!!.id)
        startActivity(edit)
    }

    /**
     * onResume, runs/is called, when resuming the app(MyNotie),
     * while android device is on sleep, or opening/switching to/from the app.
     */
    override fun onResume() {
        super.onResume()
        val folderList = NotesDB.getInstance(this)!!.foldersDAO()!!.allFolders
        updateFoldersInNavDrawer(folderList)
        ifNavMenuItemCheckedDo(folderList)
    }

    /**
     * onCreateOptionsMenu, runs/is called, when a menu is created.
     * @param menu the menu.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_bottom_top_bar, menu)
        return true
    }

    /**
     * onNoteClick, runs/is called, when a note is clicked.
     * @param note the note.
     * @param noteHolder the holder that holds content/buttons in a note.
     */
    override fun onNoteClick(note: Note?, noteHolder: NoteHolder?) {
        if (adapter!!.getCheckedNotes().size > 0 || selectNotesTopBar!!.visibility == View.VISIBLE) selectNote(
            note,
            noteHolder
        ) else {
            openNote(note)
        }
    }

    /**
     * onNoteLongClick, runs/is called, when a note is long/hold clicked.
     * @param note the note.
     * @param noteHolder the holder that holds content/buttons in a note.
     */
    override fun onNoteLongClick(note: Note?, noteHolder: NoteHolder?) {
        selectNote(note, noteHolder)
    }

    /**
     * onMoreMenuButtonClick, runs/is called, when *more menu* button(three vertical dots button) is clicked,
     * revealing a dropdown menu. to act on the specific note, the button is on.
     * @param note the note.
     * @param view the more menu button(three vertical dots button) on note.
     * @param position the position, the note is at the notes list, on *My notes* page.
     */
    override fun onMoreMenuButtonClick(note: Note, view: View, position: Int) {
        val notePopupMenu = PopupMenu(view.context, view)
        notePopupMenu.menuInflater.inflate(R.menu.menu_note_more, notePopupMenu.menu)
        notePopupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            val itemId = menuItem.itemId
            if (itemId == R.id.add_to_folder_button) {
                checkedFolders = ArrayList(notesFoldersDAO!!.getFoldersFromNote(note.id))
                unCheckedFolders = ArrayList()
                val addToFolders = Intent(this, AddToFoldersActivity::class.java)
                addToFolders.putExtra(AddToFoldersActivity.NOTE_EXTRA_KEY, note.id)
                addToFolders.putExtra(AddToFoldersActivity.NOTE_KEY, note)
                addToFolders.putParcelableArrayListExtra(AddToFoldersActivity.FOLDER_LIST_KEY, checkedFolders)
                addToFolders.putParcelableArrayListExtra(AddToFoldersActivity.UNCHECKED_FOLDERS_KEY, unCheckedFolders)
                resultLauncher.launch(addToFolders)
                Log.d(TAG, "Add to folder button, clicked.")
                return@setOnMenuItemClickListener true
            } else if (itemId == R.id.delete_only_this_note_button) {
                Log.d(TAG, "Delete note button, clicked.")
                dao!!.deleteNote(note)
                adapter!!.notes.remove(note)
                adapter!!.notesFull.remove(note)
                adapter!!.getCheckedNotes().remove(note)
                adapter!!.notifyItemRemoved(position)
                if (note.isChecked) displaySelectedNotesCount()
                if (adapter!!.getCheckedNotes().size > 0) {
                    showSelectNotesTopBar()
                } else {
                    showPageTitleTopBar()
                    displaySelectedNotesCount()
                }
                Toast.makeText(this@MyNotesActivity, "Note deleted!", Toast.LENGTH_LONG).show()
                Log.d(TAG, "Delete note button, finished.")
                return@setOnMenuItemClickListener true
            }
            false
        }
        notePopupMenu.show()
    }

    /**
     * The result launcher for managing/launching other activities and passing/getting results/data
     * from/to other activities.
     */
    private var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val note: Note? = result.data?.extras?.getParcelable(AddToFoldersActivity.NOTE_KEY)
            checkedFolders = result.data?.extras?.getParcelableArrayList(AddToFoldersActivity.FOLDER_LIST_KEY)
            unCheckedFolders = result.data?.extras?.getParcelableArrayList(AddToFoldersActivity.UNCHECKED_FOLDERS_KEY)
            saveNoteToFolders(note)
        }
    }

    companion object {
        /**
         * The id number of the *all notes button(Menu Item)*, in navigation view(side navigation drawer).
         */
        private const val ALL_NOTES_ID = -1

        /**
         * The id number of the *go to add or manage folders button(Menu Item)*, in navigation view(side navigation drawer).
         */
        private const val GO_TO_ADD_OR_MANAGE_FOLDERS_ID = -2

        /**
         * The app name.
         */
        const val TAG = "MyNotie"
    }
}