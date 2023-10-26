package com.code_that_up.john_xenakis.my_notie

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.code_that_up.john_xenakis.my_notie.db.FoldersDAO
import com.code_that_up.john_xenakis.my_notie.db.NotesDAO
import com.code_that_up.john_xenakis.my_notie.db.NotesDB
import com.code_that_up.john_xenakis.my_notie.db.NotesFoldersJoinDAO
import com.code_that_up.john_xenakis.my_notie.model.Folder
import com.code_that_up.john_xenakis.my_notie.model.Note
import com.code_that_up.john_xenakis.my_notie.model.NoteFolderJoin
import com.code_that_up.john_xenakis.my_notie.utils.FolderUtils.increaseFolderIdByOne
import com.code_that_up.john_xenakis.my_notie.utils.NoteUtils.increaseNoteIdByOne
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

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
 * <h2>MyNotesActivityTest</h2> is a class, dedicated for instrumented tests on MyNotesActivity
 * which will be executed on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 *
 * @see MyNotesActivity The class this Instrumented Test class
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class MyNotesActivityTest {
    /**
     * The Dao for notes, responsible for manipulating notes, delete, insert, update notes, in database.
     */
    private var dao: NotesDAO? = null

    /**
     * The Dao for folders, responsible for manipulating folders, delete, insert, update folders, in database.
     */
    private var foldersDao: FoldersDAO? = null

    /**
     * The Dao for joining/connecting notes with folders, and responsible for manipulating this connection,
     * delete, insert, update the connection, in database.
     */
    private var notesFoldersJoinDao: NotesFoldersJoinDAO? = null

    /**
     * The database creator, for notes.
     */
    private var notesDB: NotesDB? = null

    /**
     * addAllNotes, adds/creates all new *test notes* needed for running instrumented tests, on *MyNotesActivity*
     */
    private fun addAllNotes() {
        val folderArrayList = ArrayList<Folder>()
        for (folderNumber in 1..6) {
            folderArrayList.add(addFolder("Test folder $folderNumber"))
            folderArrayList.add(addFolder("Special folder $folderNumber"))
        }
        addNote(
            "This is a test note.",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin et ante quis nibh blandit vehicula non sit amet dui.",
            folderArrayList,
            5
        )
        addNote(
            "New note!",
            "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            folderArrayList,
            3
        )
        addNote(
            "Check this new note!",
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
            folderArrayList,
            null
        )
        addNote(
            "Cool note!",
            "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            folderArrayList,
            2
        )
        addNote(
            "Wrote it all down!",
            "Nullam imperdiet placerat porttitor. Ut ac urna sed magna gravida eleifend. ",
            folderArrayList,
            1
        )
    }

    /**
     * addNote, adds/creates each new *test note* needed for running instrumented tests.
     * @param noteTitle the note title, written on note.
     * @param noteBodyText the main text/body text, written on note.
     */
    private fun addNote(
        noteTitle: String,
        noteBodyText: String,
        folderArrayList: ArrayList<Folder>,
        folderCount: Int?
    ) {
        for (i in 0..1) {
            val testNote = Note()
            increaseNoteIdByOne(dao!!, testNote)
            val date = Date().time
            testNote.noteTitle = noteTitle
            testNote.noteBodyText = noteBodyText
            testNote.noteDate = date
            dao!!.insertNote(testNote)
            connectFolderWithNote(testNote, folderArrayList, folderCount)
        }
    }

    /**
     * Connects a folder with a note.
     * @param testNote The note to be connected with folder.
     * @param folderArrayList The folder list that contains the folders to be connected with a note.
     * @param folderCount The counting number of how many folders to be connected with a note.
     */
    private fun connectFolderWithNote(
        testNote: Note,
        folderArrayList: ArrayList<Folder>,
        folderCount: Int?
    ) {
        if (folderCount != null && folderCount <= folderArrayList.size) {
            for (a in 0 until folderCount) {
                val noteFolderJoin = NoteFolderJoin(testNote.id, folderArrayList[a].id)
                notesFoldersJoinDao!!.insertNoteFolderJoin(noteFolderJoin)
            }
        }
    }

    /**
     * Adds a new folder.
     * @param folderName The name text of the new folder.
     * @return The new folder.
     */
    private fun addFolder(folderName: String): Folder {
        val testFolder = Folder()
        increaseFolderIdByOne(foldersDao!!, testFolder)
        testFolder.folderName = folderName
        foldersDao!!.insertFolder(testFolder)
        return testFolder
    }

    /**
     * initialize, gets run everytime each test is being run/started.
     */
    @Before
    fun initialize() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        notesDB = NotesDB.getNoSaveInstance(context)
        dao = notesDB!!.notesDAO()
        foldersDao = notesDB!!.foldersDAO()
        notesFoldersJoinDao = notesDB!!.notesFoldersJoinDAO()
    }

    /**
     * tearDown, gets run everytime each test is finished/ended.
     */
    @After
    fun tearDown() {
        notesDB!!.clearAllTables()
        addAllNotes()
    }

    /**
     * The *rule* for defining what activity to test on, in this case *MyNotesActivity*.
     */
    @JvmField
    @Rule
    var myNotesActivityRule = ActivityScenarioRule(
        MyNotesActivity::class.java
    )

    /**
     * useAppContext, checks/tests, if *My Notie* have the proper package name.
     */
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.code_that_up.john_xenakis.my_notie", appContext.packageName)
    }

    /**
     * openSearchBarAndTypeInSearchEdittext, opens the Search bar and types in search edittext.
     */
    @Test
    fun openSearchBarAndTypeInSearchEdittext() {
        Espresso.onView(ViewMatchers.withId(R.id.search_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.top_app_bar_search)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.search_edittext)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.search_edittext))
            .perform(ViewActions.typeText("New note"))
    }

    /**
     * openSelectNotesTopAppbar, opens and checks if *select notes top bar* is displayed.
     */
    @Test
    fun openSelectNotesTopAppBar() {
        //Checks if "select notes top app bar" is Not displayed/closed.
        Espresso.onView(ViewMatchers.withId(R.id.top_app_bar_select_notes))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
        //Opens "select notes top app bar" by clicking "select notes button".
        Espresso.onView(ViewMatchers.withId(R.id.select_notes_button)).perform(ViewActions.click())
        //Checks if "select notes top app bar" is displayed/opened.
        Espresso.onView(ViewMatchers.withId(R.id.top_app_bar_select_notes))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * notesSelectedTitleMatchesWithText, checks if *notes selected title* or *notes count title*
     * matches with initial text, without selecting note.
     */
    @Test
    fun notesSelectedTitleMatchesWithText() {
        Espresso.onView(
            Matchers.allOf(
                Matchers.instanceOf(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_select_notes))
            )
        )
            .check(ViewAssertions.matches(ViewMatchers.withText("0 notes selected.")))
    }

    /**
     * notesSelectedTopToolbarExists, checks/asserts
     * if top bar/toolbar for notes selected, exists and is displayed on screen.
     */
    @Test
    fun notesSelectedTopToolbarExists() {
        Assert.assertNotNull(ViewMatchers.withId(R.id.top_app_bar_select_notes))
    }

    /**
     * selectingNotes, selects 3 notes
     * and checks if note count title at top bar for selecting notes,
     * displays the count number of selected notes("3 notes selected.") on screen.
     */
    @Test
    fun selectingNotes() {
        //Select 3 notes at position 0, 3 and 4.
        Espresso.onView(ViewMatchers.withId(R.id.notes_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.longClick()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.notes_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                3,
                ViewActions.click()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.notes_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                4,
                ViewActions.click()
            )
        )

        //Checks if "select notes title" at top bar displays "3 notes selected.".
        Espresso.onView(
            Matchers.allOf(
                Matchers.instanceOf(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_select_notes))
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText("3 notes selected.")))
    }

    /**
     * Checks if chip group for folders in a note, from *note list(RecyclerView)* exists/is displayed on screen.
     */
    @Test
    fun testFolderChipGroupOnNoteExists() {
        Espresso.onView(ViewMatchers.withId(R.id.notes_list)).check(
            ViewAssertions.matches(
                TestUtils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.folder_chip_group))
                )
            )
        )
        Assert.assertNotNull(ViewMatchers.withId(R.id.folder_chip_group))
    }

    /**
     * Checks if a folder chip in a note, from *note list(RecyclerView)* exists/is displayed on screen.
     */
    @Test
    fun testFolderChipOnNoteExists() {
        Espresso.onView(ViewMatchers.withId(R.id.notes_list)).check(
            ViewAssertions.matches(
                TestUtils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withContentDescription("Folder chip"))
                )
            )
        )
    }

    /**
     * selectingNotesAndMatchesNotesSelectedTitle, selects 2 notes
     * and checks if note count title at top bar for selecting notes,
     * displays the count number of selected notes("2 notes selected.") on screen.
     */
    @Test
    fun selectingNotesAndMatchesNotesSelectedTitle() {
        //Select 2 notes at position 0 and 2.
        Espresso.onView(ViewMatchers.withId(R.id.notes_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.longClick()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.notes_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                2,
                ViewActions.longClick()
            )
        )

        //Checking if "select notes title" displays "2 notes selected.".
        Espresso.onView(
            Matchers.allOf(
                Matchers.instanceOf(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_select_notes))
            )
        )
            .check(ViewAssertions.matches(ViewMatchers.withText("2 notes selected.")))
    }

    /**
     * deletingThisNoteAndCheckingNotesSelectedTitle, selects 2 notes,
     * deletes 1 of the 2 notes,
     * checks if note count title at top bar, displays "1 notes selected.".
     * @throws InterruptedException needed for Thread.sleep called method.
     */
    @Test
    @Throws(InterruptedException::class)
    fun deletingThisNoteAndCheckingNotesSelectedTitle() {
        //Selects 2 notes, by long clicking them.
        Espresso.onView(ViewMatchers.withId(R.id.notes_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.longClick()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.notes_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                3,
                ViewActions.longClick()
            )
        )

        //Clicks "more menu button" on a note, to open the popup menu.
        Espresso.onView(ViewMatchers.withId(R.id.notes_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                TestUtils.clickChildViewWithId(R.id.more_menu_button)
            )
        )
        //Thread.sleep throws Unhandled InterruptedException.
        Thread.sleep(250)

        //Clicks the "delete note." button, from the popup menu.
        Espresso.onView(ViewMatchers.withText("Delete note.")).inRoot(TestUtils.isPopupWindow)
            .perform(ViewActions.click())

        //Checks if note count title at top bar, displays "1 notes selected".
        Espresso.onView(
            Matchers.allOf(
                Matchers.instanceOf(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_select_notes))
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText("1 notes selected.")))
    }

    /**
     * testingSelectAllNotesButton, tests the *select all notes button*,
     * from top bar, which selects all notes.
     */
    @Test
    fun testingSelectAllNotesButton() {
        //Clicks "select notes button", to open "top app bar select notes".
        Espresso.onView(ViewMatchers.withId(R.id.select_notes_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.top_app_bar_select_notes))
            .perform(ViewActions.click())
        //Checks if "top app bar selected notes", displays "0 notes selected".
        Espresso.onView(
            Matchers.allOf(
                Matchers.instanceOf(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_select_notes))
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText("0 notes selected.")))

        //Clicks "select all notes" button, on "top app bar select notes", to select all notes.
        Espresso.onView(ViewMatchers.withId(R.id.select_all_notes)).perform(ViewActions.click())
        //Checks if "top app bar selected notes", displays "9 notes selected".
        Espresso.onView(
            Matchers.allOf(
                Matchers.instanceOf(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_select_notes))
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText("10 notes selected.")))

        //Clicks again "select all notes", to deselect all notes.
        Espresso.onView(ViewMatchers.withId(R.id.select_all_notes)).perform(ViewActions.click())
        //Checks if "top app bar selected notes", displays "0 notes selected".
        Espresso.onView(
            Matchers.allOf(
                Matchers.instanceOf(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_select_notes))
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText("0 notes selected.")))
    }

    /**
     * testingDeleteSelectedNotesButton, tests the *delete selected notes button*,
     * from top bar, which deletes all selected notes.
     */
    @Test
    fun testingDeleteSelectedNotesButton() {
        //Clicks "select notes button", to open "top app bar select notes".
        Espresso.onView(ViewMatchers.withId(R.id.select_notes_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.top_app_bar_select_notes))
            .perform(ViewActions.click())
        //Checks if "top app bar selected notes", displays "0 notes selected".
        Espresso.onView(
            Matchers.allOf(
                Matchers.instanceOf(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_select_notes))
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText("0 notes selected.")))

        //Clicks "select all notes" button, on "top app bar select notes", to select all notes.
        Espresso.onView(ViewMatchers.withId(R.id.select_all_notes)).perform(ViewActions.click())
        //Checks if "top app bar selected notes", displays "9 notes selected".
        Espresso.onView(
            Matchers.allOf(
                Matchers.instanceOf(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_select_notes))
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText("10 notes selected.")))

        //Clicks "delete selected notes" button, on "top app bar select notes", to delete the selected notes.
        Espresso.onView(ViewMatchers.withId(R.id.delete_selected_notes))
            .perform(ViewActions.click())
        //Checks if "top app bar selected notes", displays "0 notes selected".
        Espresso.onView(
            Matchers.allOf(
                Matchers.instanceOf(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_select_notes))
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText("0 notes selected.")))
    }

    /**
     * testUpdatingNotesCountWhenResuming, tests if *notes count title* from top bar, does get updated when app is resumed.
     */
    @Test
    fun testUpdatingNotesCountWhenResuming() {
        Espresso.onView(ViewMatchers.withId(R.id.select_notes_button)).perform(ViewActions.click())
        Espresso.onView(
            Matchers.allOf(
                Matchers.instanceOf(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_select_notes))
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText("0 notes selected.")))
        Espresso.onView(ViewMatchers.withId(R.id.select_all_notes)).perform(ViewActions.click())
        Espresso.onView(
            Matchers.allOf(
                Matchers.instanceOf(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_select_notes))
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText("10 notes selected.")))
        Espresso.onView(ViewMatchers.withId(R.id.add_new_note)).perform(ViewActions.click())
        Espresso.pressBack()
        Espresso.onView(
            Matchers.allOf(
                Matchers.instanceOf(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_select_notes))
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText("0 notes selected.")))
    }

    /**
     * testSwitchingToLandscape, tests switching from Portrait Orientation to Landscape Orientation.
     */
    @Test
    @Throws(InterruptedException::class)
    fun testSwitchingToLandscape() {
        TestUtils.switchOrientation(myNotesActivityRule, TestUtils.LANDSCAPE)
        Thread.sleep(1000)
    }

    /**
     * testSwitchingToPortrait, tests switching from Landscape Orientation to Portrait Orientation.
     */
    @Test
    @Throws(InterruptedException::class)
    fun testSwitchingToPortrait() {
        TestUtils.switchOrientation(myNotesActivityRule, TestUtils.PORTRAIT)
        Thread.sleep(1000)
    }

    /**
     * testSwitchingToReverseLandscape, tests switching from Landscape Orientation to Reverse Landscape Orientation.
     */
    @Test
    @Throws(InterruptedException::class)
    fun testSwitchingToReverseLandscape() {
        TestUtils.switchOrientation(myNotesActivityRule, TestUtils.REVERSE_LANDSCAPE)
        Thread.sleep(1000)
    }

    /**
     * testSwitchingToReversePortrait, tests switching from Portrait Orientation to Reverse Portrait Orientation.
     */
    @Test
    @Throws(InterruptedException::class)
    fun testSwitchingToReversePortrait() {
        TestUtils.switchOrientation(myNotesActivityRule, TestUtils.REVERSE_PORTRAIT)
        Thread.sleep(1000)
    }
}