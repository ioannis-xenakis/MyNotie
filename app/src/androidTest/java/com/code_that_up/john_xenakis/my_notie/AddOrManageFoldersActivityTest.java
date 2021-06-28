package com.code_that_up.john_xenakis.my_notie;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.code_that_up.john_xenakis.my_notie.TestUtils.atPosition;
import static com.code_that_up.john_xenakis.my_notie.TestUtils.clickChildViewWithId;
import static com.code_that_up.john_xenakis.my_notie.TestUtils.typeEdittextWithId;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.code_that_up.john_xenakis.my_notie.db.FoldersDAO;
import com.code_that_up.john_xenakis.my_notie.db.NotesDB;
import com.code_that_up.john_xenakis.my_notie.model.Folder;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * @see AddOrManageFoldersActivity The class this Instrumented Test class(AddOrManageFoldersActivityTest), tests on.
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class AddOrManageFoldersActivityTest {

    /**
     * The DAO for folders, responsible for manipulating folders, delete, insert, update folders, in database.
     */
    private FoldersDAO foldersDao;

    /**
     * The main core of database, unifying all DAOs and tables.
     */
    private NotesDB notesDb;

    /**
     * addAllFolders, adds/creates all new <i>test folders</i> needed for running instrumented tests, on <i>AddOrManageFoldersActivity</i>.
     */
    private void addAllFolders() {
        addFolder("My special folder");
        addFolder("Just another folder");
        addFolder("What a folder");
        addFolder("Another one?");
        addFolder("Its all about folders");
        addFolder("All I see is folders");
    }

    /**
     * addFolder, adds/creates a new <i>test folder</i>, needed for running instrumented tests.
     * @param folderNameText The folders name.
     */
    private void addFolder(String folderNameText) {
        Folder testFolder = new Folder();
        testFolder.setFolderName(folderNameText);
        foldersDao.insertFolder(testFolder);
    }

    /**
     * initialize, gets run everytime a test is being run/started.
     */
   @Before
   public void initialize() {
       Context context = ApplicationProvider.getApplicationContext();
       notesDb = NotesDB.getNoSaveInstance(context);
       foldersDao = notesDb.foldersDAO();
       addAllFolders();
   }

    /**
     * tearDown, gets run, everytime a test is finished/ended.
     */
   @After
   public void tearDown() {
       notesDb.clearAllTables();
       addAllFolders();
   }

    /**
     * The <i>rule</i> for defining what activity to test on.
     */
    @Rule
    public ActivityScenarioRule<AddOrManageFoldersActivity> addOrManageFoldersActivityRule = new ActivityScenarioRule<>(AddOrManageFoldersActivity.class);

    /**
     * testTopAppBarExists, tests that Top App Bar exists and is displayed on screen.
     */
    @Test
    public void testTopAppBarExists() {
        assertNotNull(withId(R.id.top_app_bar_add_or_manage_folders));
        onView(withId(R.id.top_app_bar_add_or_manage_folders)).check(matches(isDisplayed()));
    }

    /**
     * testAddNewFolderButtonExists, tests that Add New Folder button exists and is displayed on screen.
     */
    @Test
    public void testAddNewFolderButtonExists() {
        assertNotNull(withId(R.id.add_new_folder_button));
        onView(withId(R.id.add_new_folder_button)).check(matches(isDisplayed()));
    }

    /**
     * testAddNewFolderEdittext, tests that Add New Folder edittext exists and is displayed on screen.
     */
    @Test
    public void testNameNewFolderEdittextExists() {
        assertNotNull(withId(R.id.name_new_folder_text_input_edittext));
        onView(withId(R.id.name_new_folder_text_input_edittext)).check(matches(isDisplayed()));
    }

    /**
     * testAddNewFolderEdittextWrites, tests by writing on Edittext.
     */
    @Test
    public void testNameNewFolderEdittextWrites() {
        onView(withId(R.id.name_new_folder_text_input_edittext)).perform(typeText("My folder 1"));
        onView(withId(R.id.name_new_folder_text_input_edittext)).check(matches(withText("My folder 1")));
    }

    /**
     * testClearAddNewFolderEdittext, tests by clearing text on Edittext.
     */
    @Test
    public void testClearNameNewFolderEdittext() {
        onView(withId(R.id.name_new_folder_text_input_edittext)).check(matches(withText("")));
        onView(withId(R.id.name_new_folder_text_input_edittext)).perform(typeText("My folder 1"));
        onView(withId(R.id.name_new_folder_text_input_edittext)).check(matches(withText("My folder 1")));
        onView(withContentDescription("Clear name new folder edittext text.")).perform(click());
        onView(withId(R.id.name_new_folder_text_input_edittext)).check(matches(withText("")));
    }

    /**
     * testAddNewFolderButtonsVisibility, tests the "Add new folder" buttons visibility,
     * when user writes on "Add new folder" text field(MaterialInputEdittext).
     */
    @Test
    public void testAddNewFolderButtonsVisibility() {
        onView(withId(R.id.name_new_folder_text_input_edittext)).check(matches(withText("")));
        onView(withId(R.id.add_new_folder_button)).check(matches(isDisplayed()));
        onView(withId(R.id.accept_new_folder_button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.reject_new_folder_button)).check(matches(not(isDisplayed())));

        onView(withId(R.id.name_new_folder_text_input_edittext)).perform(typeText("My folder 1"));
        onView(withId(R.id.name_new_folder_text_input_edittext)).check(matches(withText("My folder 1")));

        onView(withId(R.id.add_new_folder_button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.accept_new_folder_button)).check(matches(isDisplayed()));
        onView(withId(R.id.reject_new_folder_button)).check(matches(isDisplayed()));
    }

    /**
     * testFolderListExists, tests if "Folder list" exists and is displayed on screen.
     */
    @Test
    public void testFolderListExists() {
        assertNotNull(withId(R.id.folder_list_add_or_manage_folders));
        onView(withId(R.id.folder_list_add_or_manage_folders)).check(matches(isDisplayed()));
    }

    /**
     * testDeleteFolderButtonExists, tests if "Delete folder button" exists and is displayed on screen.
     */
    @Test
    public void testDeleteFolderButtonExists() {
        assertNotNull(matches(atPosition(0, hasDescendant(withId(R.id.delete_folder_button)))));
        onView(withId(R.id.folder_list_add_or_manage_folders)).check(matches(atPosition(0, hasDescendant(withId(R.id.delete_folder_button)))));
    }

    /**
     * testFolderNameEdittextExists, tests if "Folder name edittext" exists and is displayed on screen.
     */
    @Test
    public void testFolderNameEdittextExists() {
        assertNotNull(matches(atPosition(0, hasDescendant(withId(R.id.folder_name_edittext)))));
        onView(withId(R.id.folder_list_add_or_manage_folders)).check(matches(atPosition(0, hasDescendant(withId(R.id.folder_name_edittext)))));
    }

    /**
     * testRenameFolderButtonsVisibility, tests the "Renaming folder" buttons visibility,
     * when user renames by typing on folder's "Folder name edittext"(MaterialInputEdittext).
     */
    @Test
    public void testRenameFolderButtonsVisibility() {
        onView(withId(R.id.folder_list_add_or_manage_folders)).check(matches(atPosition(0, hasDescendant(not(withId(R.id.accept_renaming_folder_button))))));
        onView(withId(R.id.folder_list_add_or_manage_folders)).check(matches(atPosition(0, hasDescendant(not(withId(R.id.cancel_renaming_folder_button))))));

        onView(withId(R.id.folder_list_add_or_manage_folders)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.folder_name_edittext)));
        onView(withId(R.id.folder_list_add_or_manage_folders)).perform(RecyclerViewActions.actionOnItemAtPosition(0, typeEdittextWithId(R.id.folder_name_edittext, "")));
        onView(withId(R.id.folder_list_add_or_manage_folders)).perform(RecyclerViewActions.actionOnItemAtPosition(0, typeEdittextWithId(R.id.folder_name_edittext, "Renamed folder")));

        onView(withId(R.id.folder_list_add_or_manage_folders)).check(matches(atPosition(0, hasDescendant(withText("Renamed folder")))));
        onView(withId(R.id.folder_list_add_or_manage_folders)).check(matches(atPosition(0, hasDescendant(withId(R.id.accept_renaming_folder_button)))));
        onView(withId(R.id.folder_list_add_or_manage_folders)).check(matches(atPosition(0, hasDescendant(withId(R.id.cancel_renaming_folder_button)))));
    }
}