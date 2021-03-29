package com.example.fanatic_coder.mynotepad;

import android.widget.TextView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
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
import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <h2>EditNoteActivityTest</h2> is a class, dedicated for instrumented tests on EditNoteActivity,
 * which will be executed on an Android device.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * @see com.example.fanatic_coder.mynotepad.EditNoteActivity The class this Instrumented Test class(EditNoteActivityTest), tests on.
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class EditNoteActivityTest {

    /**
     * The <i>rule</i> for defining what activity to test on.
     */
    @Rule
    public ActivityScenarioRule<EditNoteActivity> editNoteActivityRule = new ActivityScenarioRule<>(EditNoteActivity.class);

    /**
     * testNoteTitleEdittext, tests Note title, with checking if it exists/displayed on screen, writes on it,
     * and checks if the diplayed text is the correct, with the typed text.
     */
    @Test
    public void testNoteTitleEdittext() {
        String typedText = "This is a new note!";
        assertNotNull(withId(R.id.note_title));
        onView(withId(R.id.note_title)).check(matches(isDisplayed()));
        onView(withId(R.id.note_title)).perform(typeText(typedText));
        onView(withId(R.id.note_title)).check(matches(withText(typedText)));
    }

    /**
     * testClearNoteTitleTextButton, tests the clear button(circular button with an X on it).
     */
    @Test
    public void testClearNoteTitleText() {
        String typedText = "This is a new note!";
        onView(withId(R.id.note_title)).check(matches(withText("")));
        onView(withId(R.id.note_title)).perform(typeText(typedText));
        onView(withContentDescription("clear note title text")).perform(click());
        onView(withId(R.id.note_title)).check(matches(withText("")));
    }

    /**
     * testNoteBodyTextEdittext, tests Note body text with checking if it exists/displayed on screen, types on it,
     * and checks if the displayed text, is the correct with the typed text.
     */
    @Test
    public void testNoteBodyTextEdittext() {
        String typedText = "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
        "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! ";
        assertNotNull(withId(R.id.note_body_text));
        onView(withId(R.id.note_body_text)).check(matches(isDisplayed()));
        onView(withId(R.id.note_body_text)).perform(typeText(typedText));
        onView(withId(R.id.note_body_text)).check(matches(withText(typedText)));
    }

    /**
     * testClearNoteBodyTextButton, tests the clear button(circular button with an X on it), on Note Body Text Edittext.
     */
    @Test
    public void testClearNoteBodyTextButton() {
        String typedText = "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! ";
        onView(withId(R.id.note_body_text)).check(matches(withText("")));
        onView(withId(R.id.note_body_text)).perform(typeText(typedText));
        onView(withContentDescription("clear note body text")).perform(click());
        onView(withId(R.id.note_body_text)).check(matches(withText("")));
    }

    /**
     * testTopAppBarExists, tests that Top App Bar, exists and is displayed on screen.
     */
    @Test
    public void testTopAppBarExists() {
        assertNotNull(withId(R.id.top_app_bar_edit_note));
        onView(withId(R.id.top_app_bar_edit_note)).check(matches(isDisplayed()));
    }

    /**
     * testTopAppBarsTitle, tests that Top App Bar's title exists,
     * is displayed on screen
     * and has the correct title.
     */
    @Test
    public void testTopAppBarsTitle() {
        String topAppBarsTitle = "Edit note";
        assertNotNull(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_edit_note))));
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_edit_note)))).check(matches(isDisplayed()));
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.top_app_bar_edit_note)))).check(matches(withText(topAppBarsTitle)));
    }

    /**
     * testGoBackButtonExists, tests that Go Back button exists and displayed on screen.
     */
    @Test
    public void testGobackButtonExists() {
        assertNotNull(withContentDescription("Go back"));
        onView(withContentDescription("Go back")).check(matches(isDisplayed()));
    }

    /**
     * testAddToFoldersButtonExists, tests that Add To Folders button exists and is displayed on screen.
     */
    @Test
    public void testAddToFoldersButtonExists() {
        assertNotNull(withId(R.id.add_to_folders_button_edit_note));
        onView(withId(R.id.add_to_folders_button_edit_note)).check(matches(isDisplayed()));
    }

    /**
     * testRevertChangesButtonExists, tests that Revert Changes button exists and is displayed on screen.
     */
    @Test
    public void testRevertChangesButtonExists() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        assertNotNull(withText("Revert changes."));
        onView(withText("Revert changes.")).check(matches(isDisplayed()));
    }

    /**
     * testDeleteNoteButtonExists, tests that Delete Note button exists and is displayed on screen.
     */
    @Test
    public void testDeleteNoteButtonExists() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        assertNotNull(withText("Delete note."));
        onView(withText("Delete note.")).check(matches(isDisplayed()));
    }

    /**
     * testSwitchingToLandscape, tests switching from Portrait Orientation to Landscape Orientation.
     */
    @Test
    public void testSwitchingToLandscape() {
        switchOrientation(editNoteActivityRule, PORTRAIT);
        switchOrientation(editNoteActivityRule, LANDSCAPE);
    }

    /**
     * testSwitchingToPortrait, tests switching from Landscape Orientation to Portrait Orientation.
     */
    @Test
    public void testSwitchingToPortrait() {
        switchOrientation(editNoteActivityRule, LANDSCAPE);
        switchOrientation(editNoteActivityRule, PORTRAIT);
    }

    /**
     * testSwitchingToReverseLandscape, tests switching from Landscape Orientation to Reverse Landscape Orientation.
     */
    @Test
    public void testSwitchingToReverseLandscape() {
        switchOrientation(editNoteActivityRule, LANDSCAPE);
        switchOrientation(editNoteActivityRule, REVERSE_LANDSCAPE);
    }

    /**
     * testSwitchingToReversePortrait, tests switching from Portrait Orientation to Reverse Portrait Orientation.
     */
    @Test
    public void testSwitchingToReversePortrait() {
        switchOrientation(editNoteActivityRule, PORTRAIT);
        switchOrientation(editNoteActivityRule, REVERSE_PORTRAIT);
    }

}
