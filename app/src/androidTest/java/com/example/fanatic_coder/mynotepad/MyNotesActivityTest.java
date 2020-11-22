package com.example.fanatic_coder.mynotepad;

import android.content.Context;
import android.view.View;
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

import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class MyNotesActivityTest {

    private NotesDAO dao;
    private NotesDB notesDB;

    private void addAllNotes() {
        addNote("This is a test note.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin et ante quis nibh blandit vehicula non sit amet dui.");
        addNote("New note!", "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
        addNote("Check this new note!", "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
        addNote("Cool note!", "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        addNote("Wrote it all down!", "Nullam imperdiet placerat porttitor. Ut ac urna sed magna gravida eleifend. ");
    }

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

    @Before
    public void initialize() {
        Context context = ApplicationProvider.getApplicationContext();
        notesDB = NotesDB.getNoSaveInstance(context);
        dao = notesDB.notesDAO();
    }

    @After
    public void tearDown() {
        notesDB.clearAllTables();
        addAllNotes();
    }

    @Rule
    public ActivityScenarioRule<MyNotesActivity> myNotesActivityRule = new ActivityScenarioRule<>(MyNotesActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();

        assertEquals("com.example.fanatic_coder.mynotepad", appContext.getPackageName());
    }

    @Test
    public void openSearchBarAndTypeInSearchEdittext() {
        onView(withId(R.id.search_button)).perform(click());
        onView(withId(R.id.top_app_bar_search)).perform(click());
        onView(withId(R.id.search_edittext)).perform(click());
        onView(withId(R.id.search_edittext)).perform(typeText("New note"));
    }

    /*
     * Opens "select notes top app bar"
     * and checks if it is displayed.
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

    @Test
    public void notesSelectedTitleMatchesWithText() {
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes))))
                .check(matches(withText("0 notes selected.")));
    }

    @Test
    public void notesSelectedTopToolbarExists() {
        assertNotNull(withId(R.id.top_app_bar_select_notes));
    }

    @Test
    public void selectingNotes() {
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(4, click()));

        onView(allOf(Matchers.<View>instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes)))).check(matches(withText("3 notes selected.")));
    }

    /*
     * Selects two notes by long clicking them
     * and checks if "select notes title" displays "2 notes selected.".
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

    /*
     * Selects 3 notes by long clicking them,
     * opens "select notes top app bar",
     * checks if "select notes title" displays "3 notes selected.",
     * closes "select notes top app bar"
     * and checks again if "select notes title" displays "0 notes selected.".
     */
    @Test
    public void selectingNotesAndCloseNotesSelectedTopAppBar() {
        //Selecting 3 notes by long clicking at position 2, 3 and 7.
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2, longClick()));
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(3, longClick()));
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(7, longClick()));

        //Clicking on "select notes button" to open "top app bar select notes".
        onView(withId(R.id.select_notes_button)).perform(click());
        onView(withId(R.id.top_app_bar_select_notes)).perform(click());
        //Checking if "select notes title" displays "3 notes selected.".
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes))))
                .check(matches(withText("3 notes selected.")));

        //Clicking "top app bar close select notes" button to close "select notes top app bar".
        onView(withContentDescription("Close select notes toolbar")).perform(click());
        //Checking if "select notes title" displays "0 notes selected.".
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes))))
                .check(matches(withText("0 notes selected.")));
    }

    /*
     * Selects 3 notes by long clicking them,
     * checks if "select notes title" displays "3 notes selected.",
     * Types/searches notes with text, "test",
     * selecting one more note,
     * and checks again if "select notes title" displays "4 notes selected."
     */
    @Test
    public void selectingNotesSearchingNotesAndCheckingNotesSelectedTitle() throws InterruptedException {
        //Selecting 3 notes by long clicking at positions 2, 3 and 7.
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2, longClick()));
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(3, longClick()));
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(7, longClick()));
        //Checking if "select notes title" displays "3 notes selected.".
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes))))
                .check(matches(withText("3 notes selected.")));

        //Opens "search top app bar" by clicking "search button".
        onView(withId(R.id.search_button)).perform(click());
        //Types in "search edittext" "test" to search for notes.
        onView(withId(R.id.search_edittext)).perform(typeText("test")).perform(closeSoftKeyboard());
        Thread.sleep(250);
        //Selecting one more note, at position 5.
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, longClick()));

        //Checking if "select notes title" displays "4 notes selected.".
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes))))
                .check(matches(withText("4 notes selected.")));
    }

    /*
     * Selects 2 notes,
     * deletes 1 of the 2 notes,
     * checks if top app bar selected notes title, displays "1 notes selected.".
     */
    @Test
    public void deletingThisNoteAndCheckingNotesSelectedTitle() throws InterruptedException {
        //Selects 2 notes, by long clicking them.
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(3, longClick()));

        //Clicks "more menu button" on a note, to open the popup menu.
        onView(withId(R.id.notes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, TestUtils.clickChildViewWithId(R.id.more_menu_button)));
        Thread.sleep(250);

        //Clicks the "delete note." button, from the popup menu.
        onView(withText("Delete note.")).inRoot(TestUtils.isPopupWindow()).perform(click());

        //Checks if "top app bar selected notes", displays "1 notes selected".
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_select_notes)))).check(matches(withText("1 notes selected.")));
    }

    /*
     * Testing "select all notes button", on "top app bar select notes".
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

    /*
     * Testing "delete selected notes button", on "top app bar select notes".
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

    /*
     * Opens MainActivity by clicking "add new note" button
     * and check if MainActivity is opened.
     */
    @Test
    public void openMainActivityForNewNote() {
        //initializing Intents for "intended()" to work.
        init();
        //Clicks on "add new note" button(Floating Action Button or FAB).
        onView(withId(R.id.add_new_note)).perform(click());
        //Checks if MainActivity, is opened.
        intended(hasComponent(MainActivity.class.getName()));
        release();
    }

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

}
