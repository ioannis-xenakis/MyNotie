package com.code_that_up.john_xenakis.my_notie

import android.content.Intent
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
import com.code_that_up.john_xenakis.my_notie.db.FoldersDAO
import com.code_that_up.john_xenakis.my_notie.db.NotesDB
import com.code_that_up.john_xenakis.my_notie.model.Folder
import com.code_that_up.john_xenakis.my_notie.utils.FolderUtils.increaseFolderIdByOne
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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
 * <h2>AddToFoldersActivity</h2> is a class, dedicated for instrumented tests on AddToFoldersActivity,
 * which will be executed on an Android device.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see [Testing documentation](http://d.android.com/tools/testing)
 *
 * @see AddToFoldersActivity The class this Instrumented Test class
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class AddToFoldersActivityTest {
    /**
     * The DAO for folders, responsible for manipulating folders, delete, insert, update folders, in database.
     */
    private var foldersDao: FoldersDAO? = null

    /**
     * Adding the note(note id), to be added to folders, when *AddToFoldersActivity* is launched(The note is the first one in recyclerview, thus *note id = 1*).
     */
    private val intent =
        Intent(ApplicationProvider.getApplicationContext(), AddToFoldersActivity::class.java)
            .putExtra(AddToFoldersActivity.NOTE_EXTRA_KEY, 1)

    /**
     * AddAllFolders, adds/creates all new *test folders* needed for running instrumented tests, on *AddToFoldersActivity*.
     */
    private fun addAllFolders() {
        addFolder("My special folder")
        addFolder("Just another folder")
        addFolder("What a folder")
        addFolder("Another one?")
        addFolder("Its all about folders")
        addFolder("All I see is folders")
    }

    /**
     * AddFolder, adds/creates a new *test folder* needed for running instrumented tests.
     * @param folderNameText The folders name.
     */
    private fun addFolder(folderNameText: String) {
        val testFolder = Folder()
        increaseFolderIdByOne(foldersDao!!, testFolder)
        testFolder.folderName = folderNameText
        foldersDao!!.insertFolder(testFolder)
    }

    /**
     * Initialize, gets run everytime a test is being ran/started.
     */
    @Before
    fun initialize() {
        foldersDao = NotesDB.getNoSaveInstance(ApplicationProvider.getApplicationContext())!!
            .foldersDAO()
        addAllFolders()
    }

    /**
     * TearDown, gets run everytime a test is being finished/ended.
     */
    @After
    fun tearDown() {
        addAllFolders()
    }

    /**
     * The *rule* for defining  what activity to test on.
     */
    @JvmField
    @Rule
    var addToFoldersActivityRule = ActivityScenarioRule<AddToFoldersActivity>(intent)

    /**
     * testTopAppBarExists, tests that "Top App Bar" exists and is displayed on screen.
     */
    @Test
    fun testTopAppBarExists() {
        Assert.assertNotNull(ViewMatchers.withId(R.id.top_app_bar_add_to_folders))
        Espresso.onView(ViewMatchers.withId(R.id.top_app_bar_add_to_folders))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * testTopAppBarTitleIsCorrect, checks if the title text is correct.
     */
    @Test
    fun testTopAppBarTitleIsCorrect() {
        Espresso.onView(
            Matchers.allOf(
                Matchers.instanceOf(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_add_to_folders))
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText("Add to folders")))
    }

    /**
     * testTopAppBarTitleExists, checks only if "Top app bar" title exists and is displayed on screen.
     */
    @Test
    fun testTopAppBarTitleExists() {
        Assert.assertNotNull(
            Matchers.allOf(
                Matchers.instanceOf<Any>(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_add_to_folders))
            )
        )
        Espresso.onView(
            Matchers.allOf(
                Matchers.instanceOf(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_add_to_folders))
            )
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * testTopAppBarNavigationButtonExists, checks if "Top app bar" navigation button exists and is displayed on screen.
     */
    @Test
    fun testTopAppBarNavigationButtonExists() {
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withContentDescription("Go back."),
                ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_add_to_folders))
            )
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * testNameNewFolderEdittextWorks, tests if "Name new folder" Edittext works and user can type on it.
     */
    @Test
    fun testNameNewFolderEdittextWorks() {
        Espresso.onView(ViewMatchers.withId(R.id.name_new_folder_text_input_edittext))
            .perform(ViewActions.typeText("My folder 1"))
        Espresso.onView(ViewMatchers.withId(R.id.name_new_folder_text_input_edittext))
            .check(ViewAssertions.matches(ViewMatchers.withText("My folder 1")))
    }

    /**
     * testNameNewFolderEdittextExists, tests if "Name new folder" Edittext exists and is displayed on screen.
     */
    @Test
    fun testNameNewFolderEdittextExists() {
        Assert.assertNotNull(ViewMatchers.withId(R.id.name_new_folder_text_input_edittext))
        Espresso.onView(ViewMatchers.withId(R.id.name_new_folder_text_input_edittext))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * testAddNewFolderButtonExists, tests if "Add new folder button" exists and is displayed on screen.
     */
    @Test
    fun testAddNewFolderButtonExists() {
        Assert.assertNotNull(ViewMatchers.withId(R.id.add_new_folder_button))
        Espresso.onView(ViewMatchers.withId(R.id.add_new_folder_button))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * testAddNewFolderButtonsVisibility, tests the "Add new folder" buttons visibility,
     * when user writes on "Name new folder text" Edittext,
     */
    @Test
    fun testAddNewFolderButtonsVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.name_new_folder_text_input_edittext))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))
        Espresso.onView(ViewMatchers.withId(R.id.add_new_folder_button))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.accept_new_folder_button))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.reject_new_folder_button))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.name_new_folder_text_input_edittext))
            .perform(ViewActions.typeText("My folder 2"))
        Espresso.onView(ViewMatchers.withId(R.id.add_new_folder_button))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.accept_new_folder_button))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.reject_new_folder_button))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * testFolderListExists, tests if the "Folder list" exists and displayed on screen.
     */
    @Test
    fun testFolderListExists() {
        Assert.assertNotNull(ViewMatchers.withId(R.id.folder_list_add_to_folders))
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_to_folders))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * testAcceptRenamingFolderButton, tests the *Accept renaming folder button*.
     */
    @Test
    fun testAcceptRenamingFolderButton() {
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_to_folders)).check(
            ViewAssertions.matches(
                TestUtils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText(""))
                )
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_to_folders)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                TestUtils.typeEdittextWithId(R.id.folder_name_edittext, "All about Android Dev!")
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_to_folders)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                TestUtils.clickChildViewWithId(R.id.accept_renaming_folder_button)
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_to_folders)).check(
            ViewAssertions.matches(
                TestUtils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("All about Android Dev!"))
                )
            )
        )
    }

    /**
     * testCancelRenamingFolderButton, tests the *Cancel renaming folder button*.
     */
    @Test
    fun testCancelRenamingFolderButton() {
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_to_folders)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                TestUtils.typeEdittextWithId(R.id.folder_name_edittext, "More Android Dev!")
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_to_folders)).check(
            ViewAssertions.matches(
                TestUtils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("More Android Dev!"))
                )
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_to_folders)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                TestUtils.clickChildViewWithId(R.id.cancel_renaming_folder_button)
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_to_folders)).check(
            ViewAssertions.matches(
                TestUtils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(Matchers.not(ViewMatchers.withText("More Android Dev!")))
                )
            )
        )
    }
}