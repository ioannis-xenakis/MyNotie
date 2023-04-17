package com.code_that_up.john_xenakis.my_notie

import android.content.Context
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
import org.hamcrest.CoreMatchers
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
 * <h2>AddOrManageFoldersActivity</h2> is a class, dedicated for instrumented tests on AddOrManageFoldersActivity,
 * which will be executed on an Android device.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see [Testing documentation](http://d.android.com/tools/testing)
 *
 * @see AddOrManageFoldersActivity The class this Instrumented Test class
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class AddOrManageFoldersActivityTest {
    /**
     * The DAO for folders, responsible for manipulating folders, delete, insert, update folders, in database.
     */
    private var foldersDao: FoldersDAO? = null

    /**
     * The main core of database, unifying all DAOs and tables.
     */
    private var notesDb: NotesDB? = null

    /**
     * addAllFolders, adds/creates all new *test folders* needed for running instrumented tests, on *AddOrManageFoldersActivity*.
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
     * addFolder, adds/creates a new *test folder*, needed for running instrumented tests.
     * @param folderNameText The folders name.
     */
    private fun addFolder(folderNameText: String) {
        val testFolder = Folder()
        increaseFolderIdByOne(foldersDao!!, testFolder)
        testFolder.folderName = folderNameText
        foldersDao!!.insertFolder(testFolder)
    }

    /**
     * initialize, gets run everytime a test is being run/started.
     */
    @Before
    fun initialize() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        notesDb = NotesDB.getNoSaveInstance(context)
        foldersDao = notesDb!!.foldersDAO()
        addAllFolders()
    }

    /**
     * tearDown, gets run, everytime a test is finished/ended.
     */
    @After
    fun tearDown() {
        notesDb!!.clearAllTables()
        addAllFolders()
    }

    /**
     * The *rule* for defining what activity to test on.
     */
    @JvmField
    @Rule
    var addOrManageFoldersActivityRule = ActivityScenarioRule(
        AddOrManageFoldersActivity::class.java
    )

    /**
     * testTopAppBarExists, tests that Top App Bar exists and is displayed on screen.
     */
    @Test
    fun testTopAppBarExists() {
        Assert.assertNotNull(ViewMatchers.withId(R.id.top_app_bar_add_or_manage_folders))
        Espresso.onView(ViewMatchers.withId(R.id.top_app_bar_add_or_manage_folders))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * testAddNewFolderButtonExists, tests that Add New Folder button exists and is displayed on screen.
     */
    @Test
    fun testAddNewFolderButtonExists() {
        Assert.assertNotNull(ViewMatchers.withId(R.id.add_new_folder_button))
        Espresso.onView(ViewMatchers.withId(R.id.add_new_folder_button))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * testAddNewFolderEdittext, tests that Add New Folder edittext exists and is displayed on screen.
     */
    @Test
    fun testNameNewFolderEdittextExists() {
        Assert.assertNotNull(ViewMatchers.withId(R.id.name_new_folder_text_input_edittext))
        Espresso.onView(ViewMatchers.withId(R.id.name_new_folder_text_input_edittext))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * testAddNewFolderEdittextWrites, tests by writing on Edittext.
     */
    @Test
    fun testNameNewFolderEdittextWrites() {
        Espresso.onView(ViewMatchers.withId(R.id.name_new_folder_text_input_edittext))
            .perform(ViewActions.typeText("My folder 1"))
        Espresso.onView(ViewMatchers.withId(R.id.name_new_folder_text_input_edittext))
            .check(ViewAssertions.matches(ViewMatchers.withText("My folder 1")))
    }

    /**
     * testClearAddNewFolderEdittext, tests by clearing text on Edittext.
     */
    @Test
    fun testClearNameNewFolderEdittext() {
        Espresso.onView(ViewMatchers.withId(R.id.name_new_folder_text_input_edittext))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))
        Espresso.onView(ViewMatchers.withId(R.id.name_new_folder_text_input_edittext))
            .perform(ViewActions.typeText("My folder 1"))
        Espresso.onView(ViewMatchers.withId(R.id.name_new_folder_text_input_edittext))
            .check(ViewAssertions.matches(ViewMatchers.withText("My folder 1")))
        Espresso.onView(ViewMatchers.withContentDescription("Clear name new folder edittext text."))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.name_new_folder_text_input_edittext))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))
    }

    /**
     * testAddNewFolderButtonsVisibility, tests the "Add new folder" buttons visibility,
     * when user writes on "Add new folder" text field(MaterialInputEdittext).
     */
    @Test
    fun testAddNewFolderButtonsVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.name_new_folder_text_input_edittext))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))
        Espresso.onView(ViewMatchers.withId(R.id.add_new_folder_button))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.accept_new_folder_button)).check(
            ViewAssertions.matches(
                CoreMatchers.not(ViewMatchers.isDisplayed())
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.reject_new_folder_button)).check(
            ViewAssertions.matches(
                CoreMatchers.not(ViewMatchers.isDisplayed())
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.name_new_folder_text_input_edittext))
            .perform(ViewActions.typeText("My folder 1"))
        Espresso.onView(ViewMatchers.withId(R.id.name_new_folder_text_input_edittext))
            .check(ViewAssertions.matches(ViewMatchers.withText("My folder 1")))
        Espresso.onView(ViewMatchers.withId(R.id.add_new_folder_button)).check(
            ViewAssertions.matches(
                CoreMatchers.not(ViewMatchers.isDisplayed())
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.accept_new_folder_button))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.reject_new_folder_button))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * testFolderListExists, tests if "Folder list" exists and is displayed on screen.
     */
    @Test
    fun testFolderListExists() {
        Assert.assertNotNull(ViewMatchers.withId(R.id.folder_list_add_or_manage_folders))
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_or_manage_folders))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * testDeleteFolderButtonExists, tests if "Delete folder button" exists and is displayed on screen.
     */
    @Test
    fun testDeleteFolderButtonExists() {
        Assert.assertNotNull(
            ViewAssertions.matches(
                TestUtils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.delete_folder_button))
                )
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_or_manage_folders)).check(
            ViewAssertions.matches(
                TestUtils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.delete_folder_button))
                )
            )
        )
    }

    /**
     * testFolderNameEdittextExists, tests if "Folder name edittext" exists and is displayed on screen.
     */
    @Test
    fun testFolderNameEdittextExists() {
        Assert.assertNotNull(
            ViewAssertions.matches(
                TestUtils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.folder_name_edittext))
                )
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_or_manage_folders)).check(
            ViewAssertions.matches(
                TestUtils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.folder_name_edittext))
                )
            )
        )
    }

    /**
     * testRenameFolderButtonsVisibility, tests the "Renaming folder" buttons visibility,
     * when user renames by typing on folder's "Folder name edittext"(MaterialInputEdittext).
     */
    @Test
    fun testRenameFolderButtonsVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_or_manage_folders)).check(
            ViewAssertions.matches(
                TestUtils.atPosition(
                    0, ViewMatchers.hasDescendant(
                        CoreMatchers.not(ViewMatchers.withId(R.id.accept_renaming_folder_button))
                    )
                )
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_or_manage_folders)).check(
            ViewAssertions.matches(
                TestUtils.atPosition(
                    0, ViewMatchers.hasDescendant(
                        CoreMatchers.not(ViewMatchers.withId(R.id.cancel_renaming_folder_button))
                    )
                )
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_or_manage_folders)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                TestUtils.clickChildViewWithId(R.id.folder_name_edittext)
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_or_manage_folders)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                TestUtils.typeEdittextWithId(R.id.folder_name_edittext, "")
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_or_manage_folders)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                TestUtils.typeEdittextWithId(R.id.folder_name_edittext, "Renamed folder")
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_or_manage_folders)).check(
            ViewAssertions.matches(
                TestUtils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("Renamed folder"))
                )
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_or_manage_folders)).check(
            ViewAssertions.matches(
                TestUtils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.accept_renaming_folder_button))
                )
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.folder_list_add_or_manage_folders)).check(
            ViewAssertions.matches(
                TestUtils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.cancel_renaming_folder_button))
                )
            )
        )
    }
}