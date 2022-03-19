package com.code_that_up.john_xenakis.my_notie;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.code_that_up.john_xenakis.my_notie.TestUtils.atPosition;
import static com.code_that_up.john_xenakis.my_notie.TestUtils.clickChildViewWithId;
import static com.code_that_up.john_xenakis.my_notie.TestUtils.typeEdittextWithId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.code_that_up.john_xenakis.my_notie.db.FoldersDAO;
import com.code_that_up.john_xenakis.my_notie.db.NotesDB;
import com.code_that_up.john_xenakis.my_notie.model.Folder;
import com.code_that_up.john_xenakis.my_notie.utils.FolderUtils;

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
 * <h2>AddToFoldersActivity</h2> is a class, dedicated for instrumented tests on AddToFoldersActivity,
 * which will be executed on an Android device.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * @see AddToFoldersActivity The class this Instrumented Test class(AddToFoldersActivityTest), tests on.
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class AddToFoldersActivityTest {

    /**
     * The DAO for folders, responsible for manipulating folders, delete, insert, update folders, in database.
     */
    private FoldersDAO foldersDao;

    /**
     * Adding the note(note id), to be added to folders, when <i>AddToFoldersActivity</i> is launched(The note is the first one in recyclerview, thus <i>note id = 1</i>).
     */
    private final Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AddToFoldersActivity.class)
            .putExtra(AddToFoldersActivity.NOTE_EXTRA_KEY, 1);

    /**
     * AddAllFolders, adds/creates all new <i>test folders</i> needed for running instrumented tests, on <i>AddToFoldersActivity</i>.
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
     * AddFolder, adds/creates a new <i>test folder</i> needed for running instrumented tests.
     * @param folderNameText The folders name.
     */
    private void addFolder(String folderNameText) {
        Folder testFolder = new Folder();
        FolderUtils.increaseFolderIdByOne(foldersDao, testFolder);
        testFolder.setFolderName(folderNameText);
        foldersDao.insertFolder(testFolder);
    }

    /**
     * Initialize, gets run everytime a test is being ran/started.
     */
    @Before
    public void initialize() {
        foldersDao = NotesDB.getNoSaveInstance(ApplicationProvider.getApplicationContext()).foldersDAO();
        addAllFolders();
    }

    /**
     * TearDown, gets run everytime a test is being finished/ended.
     */
    @After
    public void tearDown() {
        addAllFolders();
    }

    /**
     * The <i>rule</i> for defining  what activity to test on.
     */
    @Rule
    public ActivityScenarioRule<AddToFoldersActivity> addToFoldersActivityRule = new ActivityScenarioRule<>(intent);

    /**
     * testTopAppBarExists, tests that "Top App Bar" exists and is displayed on screen.
     */
    @Test
    public void testTopAppBarExists() {
        assertNotNull(withId(R.id.top_app_bar_add_to_folders));
        onView(withId(R.id.top_app_bar_add_to_folders)).check(matches(isDisplayed()));
    }

    /**
     * testTopAppBarTitleIsCorrect, checks if the title text is correct.
     */
    @Test
    public void testTopAppBarTitleIsCorrect() {
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_add_to_folders)))).check(matches(withText("Add to folders")));
    }

    /**
     * testTopAppBarTitleExists, checks only if "Top app bar" title exists and is displayed on screen.
     */
    @Test
    public void testTopAppBarTitleExists() {
        assertNotNull(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_add_to_folders))));
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_add_to_folders)))).check(matches(isDisplayed()));
    }

    /**
     * testTopAppBarNavigationButtonExists, checks if "Top app bar" navigation button exists and is displayed on screen.
     */
    @Test
    public void testTopAppBarNavigationButtonExists() {
        onView(allOf(withContentDescription("Go back."), withParent(withId(R.id.top_app_bar_add_to_folders)))).check(matches(isDisplayed()));
    }

    /**
     * testNameNewFolderEdittextWorks, tests if "Name new folder" Edittext works and user can type on it.
     */
    @Test
    public void testNameNewFolderEdittextWorks() {
        onView(withId(R.id.name_new_folder_text_input_edittext)).perform(typeText("My folder 1"));
        onView(withId(R.id.name_new_folder_text_input_edittext)).check(matches(withText("My folder 1")));
    }

    /**
     * testNameNewFolderEdittextExists, tests if "Name new folder" Edittext exists and is displayed on screen.
     */
    @Test
    public void testNameNewFolderEdittextExists() {
        assertNotNull(withId(R.id.name_new_folder_text_input_edittext));
        onView(withId(R.id.name_new_folder_text_input_edittext)).check(matches(isDisplayed()));
    }

    /**
     * testAddNewFolderButtonExists, tests if "Add new folder button" exists and is displayed on screen.
     */
    @Test
    public void testAddNewFolderButtonExists() {
        assertNotNull(withId(R.id.add_new_folder_button));
        onView(withId(R.id.add_new_folder_button)).check(matches(isDisplayed()));
    }

    /**
     * testAddNewFolderButtonsVisibility, tests the "Add new folder" buttons visibility,
     * when user writes on "Name new folder text" Edittext,
     */
    @Test
    public void testAddNewFolderButtonsVisibility() {
        onView(withId(R.id.name_new_folder_text_input_edittext)).check(matches(withText("")));
        onView(withId(R.id.add_new_folder_button)).check(matches(isDisplayed()));
        onView(withId(R.id.accept_new_folder_button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.reject_new_folder_button)).check(matches(not(isDisplayed())));

        onView(withId(R.id.name_new_folder_text_input_edittext)).perform(typeText("My folder 2"));
        onView(withId(R.id.add_new_folder_button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.accept_new_folder_button)).check(matches(isDisplayed()));
        onView(withId(R.id.reject_new_folder_button)).check(matches(isDisplayed()));
    }

    /**
     * testFolderListExists, tests if the "Folder list" exists and displayed on screen.
     */
    @Test
    public void testFolderListExists() {
        assertNotNull(withId(R.id.folder_list_add_to_folders));
        onView(withId(R.id.folder_list_add_to_folders)).check(matches(isDisplayed()));
    }

    /**
     * testAcceptRenamingFolderButton, tests the <i>Accept renaming folder button</i>.
     */
    @Test
    public void testAcceptRenamingFolderButton() {
        onView(withId(R.id.folder_list_add_to_folders)).check(matches(atPosition(0, hasDescendant(withText("")))));
        onView(withId(R.id.folder_list_add_to_folders)).perform(RecyclerViewActions.actionOnItemAtPosition(0, typeEdittextWithId(R.id.folder_name_edittext, "All about Android Dev!")));
        onView(withId(R.id.folder_list_add_to_folders)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.accept_renaming_folder_button)));
        onView(withId(R.id.folder_list_add_to_folders)).check(matches(atPosition(0, hasDescendant(withText("All about Android Dev!")))));
    }

    /**
     * testCancelRenamingFolderButton, tests the <i>Cancel renaming folder button</i>.
     */
    @Test
    public void testCancelRenamingFolderButton() {
        onView(withId(R.id.folder_list_add_to_folders)).perform(RecyclerViewActions.actionOnItemAtPosition(0, typeEdittextWithId(R.id.folder_name_edittext, "More Android Dev!")));
        onView(withId(R.id.folder_list_add_to_folders)).check(matches(atPosition(0, hasDescendant(withText("More Android Dev!")))));
        onView(withId(R.id.folder_list_add_to_folders)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.cancel_renaming_folder_button)));
        onView(withId(R.id.folder_list_add_to_folders)).check(matches(atPosition(0, hasDescendant(not(withText("More Android Dev!"))))));
    }
}