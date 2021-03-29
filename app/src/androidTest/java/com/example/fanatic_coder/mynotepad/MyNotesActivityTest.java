package com.example.fanatic_coder.mynotepad;

import android.content.Context;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.fanatic_coder.mynotepad.db.NotesDAO;
import com.example.fanatic_coder.mynotepad.db.NotesDB;
import com.example.fanatic_coder.mynotepad.model.Note;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.example.fanatic_coder.mynotepad.TestUtils.LANDSCAPE;
import static com.example.fanatic_coder.mynotepad.TestUtils.PORTRAIT;
import static com.example.fanatic_coder.mynotepad.TestUtils.REVERSE_LANDSCAPE;
import static com.example.fanatic_coder.mynotepad.TestUtils.REVERSE_PORTRAIT;
import static com.example.fanatic_coder.mynotepad.TestUtils.switchOrientation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * <h2>MyNotesActivityTest</h2> is a class, dedicated for instrumented tests on MyNotesActivity
 * which will be executed on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * @see com.example.fanatic_coder.mynotepad.MyNotesActivity The class this Instrumented Test class(MyNotesActivityTest), tests on.
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class MyNotesActivityTest {

    /**
     * The Dao for notes, responsible for manipulating notes, delete, insert, update notes, in database.
     */
    private NotesDAO dao;

    /**
     * The database creator, for notes.
     */
    private NotesDB notesDB;

    /**
     * addAllNotes, adds/creates all new <i>test notes</i> needed for running instrumented tests, on <i>MyNotesActivity</i>
     */
    private void addAllNotes() {
        addNote("This is a test note.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin et ante quis nibh blandit vehicula non sit amet dui.");
        addNote("New note!", "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
        addNote("Check this new note!", "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
        addNote("Cool note!", "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        addNote("Wrote it all down!", "Nullam imperdiet placerat porttitor. Ut ac urna sed magna gravida eleifend. ");
    }

    /**
     * addNote, adds/creates each new <i>test note</i> needed for running instrumented tests.
     * @param noteTitle the note title, written on note.
     * @param noteBodyText the main text/body text, written on note.
     */
    private void addNote(String noteTitle, String noteBodyText) {
        for (int i=0; i<2; i++) {
            Note testNote = new Note();
            long date = new Date().getTime();
            testNote.setNoteTitle(noteTitle);
            testNote.setNoteBodyText(noteBodyText);
            testNote.setNoteDate(date);
            dao.insertNote(testNote);
        }
    }

    /**
     * initialize, gets run everytime each test is being run/started.
     */
    @Before
    public void initialize() {
        Context context = ApplicationProvider.getApplicationContext();
        notesDB = NotesDB.getNoSaveInstance(context);
        dao = notesDB.notesDAO();
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
     * useAppContext, checks/tests, if <i>My Notie</i> gets run/loaded.
     */
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();

        assertEquals("com.example.fanatic_coder.mynotepad", appContext.getPackageName());
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
        switchOrientation(myNotesActivityRule, LANDSCAPE);
        Thread.sleep(1000);
    }

    /**
     * testSwitchingToPortrait, tests switching from Landscape Orientation to Portrait Orientation.
     */
    @Test
    public void testSwitchingToPortrait() throws InterruptedException {
        switchOrientation(myNotesActivityRule, PORTRAIT);
        Thread.sleep(1000);
    }

    /**
     * testSwitchingToReverseLandscape, tests switching from Landscape Orientation to Reverse Landscape Orientation.
     */
    @Test
    public void testSwitchingToReverseLandscape() throws InterruptedException {
        switchOrientation(myNotesActivityRule, REVERSE_LANDSCAPE);
        Thread.sleep(1000);
    }

    /**
     * testSwitchingToReversePortrait, tests switching from Portrait Orientation to Reverse Portrait Orientation.
     */
    @Test
    public void testSwitchingToReversePortrait() throws InterruptedException {
        switchOrientation(myNotesActivityRule, REVERSE_PORTRAIT);
        Thread.sleep(1000);
    }

}
