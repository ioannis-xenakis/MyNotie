package com.code_that_up.john_xenakis.my_notie;

import android.content.Context;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.code_that_up.john_xenakis.my_notie.db.FoldersDAO;
import com.code_that_up.john_xenakis.my_notie.db.NotesDAO;
import com.code_that_up.john_xenakis.my_notie.db.NotesDB;
import com.code_that_up.john_xenakis.my_notie.db.NotesFoldersJoinDAO;
import com.code_that_up.john_xenakis.my_notie.model.Folder;
import com.code_that_up.john_xenakis.my_notie.model.Note;
import com.code_that_up.john_xenakis.my_notie.model.NoteFolderJoin;
import com.code_that_up.john_xenakis.my_notie.utils.FolderUtils;
import com.code_that_up.john_xenakis.my_notie.utils.NoteUtils;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.code_that_up.john_xenakis.my_notie.TestUtils.atPosition;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
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
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * @see MyNotesActivity The class this Instrumented Test class(MyNotesActivityTest), tests on.
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class MyNotesActivityTest {

    /**
     * The Dao for notes, responsible for manipulating notes, delete, insert, update notes, in database.
     */
    private NotesDAO dao;

    /**
     * The Dao for folders, responsible for manipulating folders, delete, insert, update folders, in database.
     */
    private FoldersDAO foldersDao;

    /**
     * The Dao for joining/connecting notes with folders, and responsible for manipulating this connection,
     * delete, insert, update the connection, in database.
     */
    private NotesFoldersJoinDAO notesFoldersJoinDao;

    /**
     * The database creator, for notes.
     */
    private NotesDB notesDB;

    /**
     * addAllNotes, adds/creates all new <i>test notes</i> needed for running instrumented tests, on <i>MyNotesActivity</i>
     */
    private void addAllNotes() {
        ArrayList<Folder> folderArrayList = new ArrayList<>();

        for (int folderNumber=1; folderNumber<=6; folderNumber++) {
            folderArrayList.add(addFolder("Test folder " + folderNumber));
            folderArrayList.add(addFolder("Special folder " + folderNumber));
        }

        addNote("This is a test note.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin et ante quis nibh blandit vehicula non sit amet dui.",
                folderArrayList, 5);
        addNote("New note!", "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                folderArrayList, 3);
        addNote("Check this new note!", "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
                folderArrayList, null);
        addNote("Cool note!", "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                folderArrayList, 2);
        addNote("Wrote it all down!", "Nullam imperdiet placerat porttitor. Ut ac urna sed magna gravida eleifend. ",
                folderArrayList, 1);
    }

    /**
     * addNote, adds/creates each new <i>test note</i> needed for running instrumented tests.
     * @param noteTitle the note title, written on note.
     * @param noteBodyText the main text/body text, written on note.
     */
    private void addNote(String noteTitle, String noteBodyText, ArrayList<Folder> folderArrayList, Integer folderCount) {
        for (int i=0; i<2; i++) {
            Note testNote = new Note();
            NoteUtils.increaseNoteIdByOne(dao, testNote);
            long date = new Date().getTime();
            testNote.setNoteTitle(noteTitle);
            testNote.setNoteBodyText(noteBodyText);
            testNote.setNoteDate(date);

            dao.insertNote(testNote);
            connectFolderWithNote(testNote, folderArrayList, folderCount);
        }
    }

    /**
     * Connects a folder with a note.
     * @param testNote The note to be connected with folder.
     * @param folderArrayList The folder list that contains the folders to be connected with a note.
     * @param folderCount The counting number of how many folders to be connected with a note.
     */
    private void connectFolderWithNote(Note testNote, ArrayList<Folder> folderArrayList, Integer folderCount) {
        if (folderCount != null && folderCount <= folderArrayList.size()) {
            for (int a = 0; a < folderCount; a++) {
                NoteFolderJoin noteFolderJoin = new NoteFolderJoin(testNote.getId(), folderArrayList.get(a).getId());
                notesFoldersJoinDao.insertNoteFolderJoin(noteFolderJoin);
            }
        }
    }

    /**
     * Adds a new folder.
     * @param folderName The name text of the new folder.
     * @return The new folder.
     */
    private Folder addFolder(String folderName) {
        Folder testFolder = new Folder();
        FolderUtils.increaseFolderIdByOne(foldersDao, testFolder);
        testFolder.setFolderName(folderName);
        foldersDao.insertFolder(testFolder);
        return testFolder;
    }

    /**
     * initialize, gets run everytime each test is being run/started.
     */
    @Before
    public void initialize() {
        Context context = ApplicationProvider.getApplicationContext();
        notesDB = NotesDB.getNoSaveInstance(context);
        dao = notesDB.notesDAO();
        foldersDao = notesDB.foldersDAO();
        notesFoldersJoinDao = notesDB.notesFoldersJoinDAO();
    }

    /**
     * tearDown, gets run everytime each test is finished/ended.
     */
    @After
    public void tearDown() {
        notesDB.clearAllTables();
        addAllNotes();
    }

    /**
     * The <i>rule</i> for defining what activity to test on, in this case <i>MyNotesActivity</i>.
     */
    @Rule
    public ActivityScenarioRule<MyNotesActivity> myNotesActivityRule = new ActivityScenarioRule<>(MyNotesActivity.class);

    /**
     * useAppContext, checks/tests, if <i>My Notie</i> have the proper package name.
     */
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();

        assertEquals("com.code_that_up.john_xenakis.my_notie", appContext.getPackageName());
    }

    /**
     * openSearchBarAndTypeInSearchEdittext, opens the Search bar and types in search edittext.
     */
    @Test
    public void openSearchBarAndTypeInSearchEdittext() {
        onView(withId(R.id.search_button)).perform(click());
        onView(withId(R.id.top_app_bar_search)).perform(click());
        onView(withId(R.id.search_edittext)).perform(click());
        onView(withId(R.id.search_edittext)).perform(typeText("New note"));
    }

    /**
     * openSelectNotesTopAppbar, opens and checks if <i>select notes top bar</i> is displayed.
     */
    @Test
    public void openSelectNotesTopAppBar() {
        //Checks if "select notes top app bar" is Not displayed/closed.
        onView(withId(R.id.top_app_bar_select_notes)).check(matches(not(isDisplayed())));
        //Opens "select notes top app bar" by clicking "select notes button".
        onView(withId(R.id.select_notes_button)).perform(click());
        //Checks if "select notes top app bar" is displayed/opened.
        onView(withId(R.id.top_app_bar_select_notes)).check(matches(isDisplayed()));
    }

    /**
     * notesSelectedTitleMatchesWithText, checks if <i>notes selected title</i> or <i>notes count title</i>
     * matches with initial text, without selecting note.
     */
    @Test
    public void notesSelectedTitleMatchesWithText() {
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes))))
                .check(matches(withText("0 notes selected.")));
    }

    /**
     * notesSelectedTopToolbarExists, checks/asserts
     * if top bar/toolbar for notes selected, exists and is displayed on screen.
     */
    @Test
    public void notesSelectedTopToolbarExists() {
        assertNotNull(withId(R.id.top_app_bar_select_notes));
    }

    /**
     * selectingNotes, selects 3 notes
     * and checks if note count title at top bar for selecting notes,
     * displays the count number of selected notes("3 notes selected.") on screen.
     */
    @Test
    public void selectingNotes() {
        //Select 3 notes at position 0, 3 and 4.
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(4, click()));

        //Checks if "select notes title" at top bar displays "3 notes selected.".
        onView(allOf(Matchers.instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes)))).check(matches(withText("3 notes selected.")));
    }

    /**
     * Checks if chip group for folders in a note, from <i>note list(RecyclerView)</i> exists/is displayed on screen.
     */
    @Test
    public void testFolderChipGroupOnNoteExists() {
        onView(withId(R.id.notes_list)).check(matches(atPosition(0, hasDescendant(withId(R.id.folder_chip_group)))));
        assertNotNull(withId(R.id.folder_chip_group));
    }

    /**
     * Checks if a folder chip in a note, from <i>note list(RecyclerView)</i> exists/is displayed on screen.
     */
    @Test
    public void testFolderChipOnNoteExists() {
        onView(withId(R.id.notes_list)).check(matches(atPosition(0, hasDescendant(withContentDescription("Folder chip")))));
    }

    /**
     * selectingNotesAndMatchesNotesSelectedTitle, selects 2 notes
     * and checks if note count title at top bar for selecting notes,
     * displays the count number of selected notes("2 notes selected.") on screen.
     */
    @Test
    public void selectingNotesAndMatchesNotesSelectedTitle() {
        //Select 2 notes at position 0 and 2.
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2, longClick()));

        //Checking if "select notes title" displays "2 notes selected.".
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes))))
                .check(matches(withText("2 notes selected.")));
    }

    /**
     * deletingThisNoteAndCheckingNotesSelectedTitle, selects 2 notes,
     * deletes 1 of the 2 notes,
     * checks if note count title at top bar, displays "1 notes selected.".
     * @throws InterruptedException needed for Thread.sleep called method.
     */
    @Test
    public void deletingThisNoteAndCheckingNotesSelectedTitle() throws InterruptedException {
        //Selects 2 notes, by long clicking them.
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(3, longClick()));

        //Clicks "more menu button" on a note, to open the popup menu.
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, TestUtils.clickChildViewWithId(R.id.more_menu_button)));
        //Thread.sleep throws Unhandled InterruptedException.
        Thread.sleep(250);

        //Clicks the "delete note." button, from the popup menu.
        onView(withText("Delete note.")).inRoot(TestUtils.isPopupWindow()).perform(click());

        //Checks if note count title at top bar, displays "1 notes selected".
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes)))).check(matches(withText("1 notes selected.")));
    }

    /**
     * testingSelectAllNotesButton, tests the <i>select all notes button</i>,
     * from top bar, which selects all notes.
     */
    @Test
    public void testingSelectAllNotesButton() {
        //Clicks "select notes button", to open "top app bar select notes".
        onView(withId(R.id.select_notes_button)).perform(click());
        onView(withId(R.id.top_app_bar_select_notes)).perform(click());
        //Checks if "top app bar selected notes", displays "0 notes selected".
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes)))).check(matches(withText("0 notes selected.")));

        //Clicks "select all notes" button, on "top app bar select notes", to select all notes.
        onView(withId(R.id.select_all_notes)).perform(click());
        //Checks if "top app bar selected notes", displays "9 notes selected".
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes)))).check(matches(withText("10 notes selected.")));

        //Clicks again "select all notes", to deselect all notes.
        onView(withId(R.id.select_all_notes)).perform(click());
        //Checks if "top app bar selected notes", displays "0 notes selected".
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes)))).check(matches(withText("0 notes selected.")));
    }

    /**
     * testingDeleteSelectedNotesButton, tests the <i>delete selected notes button</i>,
     * from top bar, which deletes all selected notes.
     */
    @Test
    public void testingDeleteSelectedNotesButton() {
        //Clicks "select notes button", to open "top app bar select notes".
        onView(withId(R.id.select_notes_button)).perform(click());
        onView(withId(R.id.top_app_bar_select_notes)).perform(click());
        //Checks if "top app bar selected notes", displays "0 notes selected".
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes)))).check(matches(withText("0 notes selected.")));

        //Clicks "select all notes" button, on "top app bar select notes", to select all notes.
        onView(withId(R.id.select_all_notes)).perform(click());
        //Checks if "top app bar selected notes", displays "9 notes selected".
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes)))).check(matches(withText("10 notes selected.")));

        //Clicks "delete selected notes" button, on "top app bar select notes", to delete the selected notes.
        onView(withId(R.id.delete_selected_notes)).perform(click());
        //Checks if "top app bar selected notes", displays "0 notes selected".
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes)))).check(matches(withText("0 notes selected.")));
    }

    /**
     * testUpdatingNotesCountWhenResuming, tests if <i>notes count title</i> from top bar, does get updated when app is resumed.
     */
    @Test
    public void testUpdatingNotesCountWhenResuming() {
        onView(withId(R.id.select_notes_button)).perform(click());
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes)))).check(matches(withText("0 notes selected.")));
        onView(withId(R.id.select_all_notes)).perform(click());
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes)))).check(matches(withText("10 notes selected.")));
        onView(withId(R.id.add_new_note)).perform(click());
        Espresso.pressBack();
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes)))).check(matches(withText("0 notes selected.")));
    }

    /**
     * testSwitchingToLandscape, tests switching from Portrait Orientation to Landscape Orientation.
     */
    @Test
    public void testSwitchingToLandscape() throws InterruptedException {
        TestUtils.switchOrientation(myNotesActivityRule, TestUtils.LANDSCAPE);
        Thread.sleep(1000);
    }

    /**
     * testSwitchingToPortrait, tests switching from Landscape Orientation to Portrait Orientation.
     */
    @Test
    public void testSwitchingToPortrait() throws InterruptedException {
        TestUtils.switchOrientation(myNotesActivityRule, TestUtils.PORTRAIT);
        Thread.sleep(1000);
    }

    /**
     * testSwitchingToReverseLandscape, tests switching from Landscape Orientation to Reverse Landscape Orientation.
     */
    @Test
    public void testSwitchingToReverseLandscape() throws InterruptedException {
        TestUtils.switchOrientation(myNotesActivityRule, TestUtils.REVERSE_LANDSCAPE);
        Thread.sleep(1000);
    }

    /**
     * testSwitchingToReversePortrait, tests switching from Portrait Orientation to Reverse Portrait Orientation.
     */
    @Test
    public void testSwitchingToReversePortrait() throws InterruptedException {
        TestUtils.switchOrientation(myNotesActivityRule, TestUtils.REVERSE_PORTRAIT);
        Thread.sleep(1000);
    }

}
