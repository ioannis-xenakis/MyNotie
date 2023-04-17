package com.code_that_up.john_xenakis.my_notie

import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers
import org.junit.Assert
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
 * <h2>EditNoteActivityTest</h2> is a class, dedicated for instrumented tests on EditNoteActivity,
 * which will be executed on an Android device.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see [Testing documentation](http://d.android.com/tools/testing)
 *
 * @see EditNoteActivity The class this Instrumented Test class
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class EditNoteActivityTest {
    /**
     * The *rule* for defining what activity to test on.
     */
    @JvmField
    @Rule
    var editNoteActivityRule = ActivityScenarioRule(
        EditNoteActivity::class.java
    )

    /**
     * testNoteTitleEdittext, tests Note title, with checking if it exists/displayed on screen, writes on it,
     * and checks if the diplayed text is the correct, with the typed text.
     */
    @Test
    fun testNoteTitleEdittext() {
        val typedText = "This is a new note!"
        Assert.assertNotNull(ViewMatchers.withId(R.id.note_title))
        Espresso.onView(ViewMatchers.withId(R.id.note_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.note_title))
            .perform(ViewActions.typeText(typedText))
        Espresso.onView(ViewMatchers.withId(R.id.note_title))
            .check(ViewAssertions.matches(ViewMatchers.withText(typedText)))
    }

    /**
     * testClearNoteTitleTextButton, tests the clear button(circular button with an X on it).
     */
    @Test
    fun testClearNoteTitleText() {
        val typedText = "This is a new note!"
        Espresso.onView(ViewMatchers.withId(R.id.note_title))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))
        Espresso.onView(ViewMatchers.withId(R.id.note_title))
            .perform(ViewActions.typeText(typedText))
        Espresso.onView(ViewMatchers.withContentDescription("clear note title text"))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.note_title))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))
    }

    /**
     * testNoteBodyTextEdittext, tests Note body text with checking if it exists/displayed on screen, types on it,
     * and checks if the displayed text, is the correct with the typed text.
     */
    @Test
    fun testNoteBodyTextEdittext() {
        val typedText =
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
                    "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                    "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! "
        Assert.assertNotNull(ViewMatchers.withId(R.id.note_body_text))
        Espresso.onView(ViewMatchers.withId(R.id.note_body_text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.note_body_text))
            .perform(ViewActions.typeText(typedText))
        Espresso.onView(ViewMatchers.withId(R.id.note_body_text))
            .check(ViewAssertions.matches(ViewMatchers.withText(typedText)))
    }

    /**
     * testClearNoteBodyTextButton, tests the clear button(circular button with an X on it), on Note Body Text Edittext.
     */
    @Test
    fun testClearNoteBodyTextButton() {
        val typedText =
            "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                    "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                    "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                    "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                    "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! " +
                    "This is a new note with main note text! This is a new note with main note text! This is a new note with main note text! "
        Espresso.onView(ViewMatchers.withId(R.id.note_body_text))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))
        Espresso.onView(ViewMatchers.withId(R.id.note_body_text))
            .perform(ViewActions.typeText(typedText), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withContentDescription("clear note body text"))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.note_body_text))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))
    }

    /**
     * testTopAppBarExists, tests that Top App Bar, exists and is displayed on screen.
     */
    @Test
    fun testTopAppBarExists() {
        Assert.assertNotNull(ViewMatchers.withId(R.id.top_app_bar_edit_note))
        Espresso.onView(ViewMatchers.withId(R.id.top_app_bar_edit_note))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * testTopAppBarsTitle, tests that Top App Bar's title exists,
     * is displayed on screen
     * and has the correct title.
     */
    @Test
    fun testTopAppBarsTitle() {
        val topAppBarsTitle = "Edit note"
        Assert.assertNotNull(
            Matchers.allOf(
                Matchers.instanceOf<Any>(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_edit_note))
            )
        )
        Espresso.onView(
            Matchers.allOf(
                Matchers.instanceOf(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_edit_note))
            )
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(
            Matchers.allOf(
                Matchers.instanceOf(
                    TextView::class.java
                ), ViewMatchers.withParent(ViewMatchers.withId(R.id.top_app_bar_edit_note))
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText(topAppBarsTitle)))
    }

    /**
     * testGoBackButtonExists, tests that Go Back button exists and displayed on screen.
     */
    @Test
    fun testGobackButtonExists() {
        Assert.assertNotNull(ViewMatchers.withContentDescription("Go back"))
        Espresso.onView(ViewMatchers.withContentDescription("Go back"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * testAddToFoldersButtonExists, tests that Add To Folders button exists and is displayed on screen.
     */
    @Test
    fun testAddToFoldersButtonExists() {
        Assert.assertNotNull(ViewMatchers.withId(R.id.add_to_folders_button_edit_note))
        Espresso.onView(ViewMatchers.withId(R.id.add_to_folders_button_edit_note))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * testRevertChangesButtonExists, tests that Revert Changes button exists and is displayed on screen.
     */
    @Test
    fun testRevertChangesButtonExists() {
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        Assert.assertNotNull(ViewMatchers.withText("Revert changes."))
        Espresso.onView(ViewMatchers.withText("Revert changes."))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * testDeleteNoteButtonExists, tests that Delete Note button exists and is displayed on screen.
     */
    @Test
    fun testDeleteNoteButtonExists() {
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        Assert.assertNotNull(ViewMatchers.withText("Delete note."))
        Espresso.onView(ViewMatchers.withText("Delete note."))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * testSwitchingToLandscape, tests switching from Portrait Orientation to Landscape Orientation.
     */
    @Test
    fun testSwitchingToLandscape() {
        TestUtils.switchOrientation(editNoteActivityRule, TestUtils.PORTRAIT)
        TestUtils.switchOrientation(editNoteActivityRule, TestUtils.LANDSCAPE)
    }

    /**
     * testSwitchingToPortrait, tests switching from Landscape Orientation to Portrait Orientation.
     */
    @Test
    fun testSwitchingToPortrait() {
        TestUtils.switchOrientation(editNoteActivityRule, TestUtils.LANDSCAPE)
        TestUtils.switchOrientation(editNoteActivityRule, TestUtils.PORTRAIT)
    }

    /**
     * testSwitchingToReverseLandscape, tests switching from Landscape Orientation to Reverse Landscape Orientation.
     */
    @Test
    fun testSwitchingToReverseLandscape() {
        TestUtils.switchOrientation(editNoteActivityRule, TestUtils.LANDSCAPE)
        TestUtils.switchOrientation(editNoteActivityRule, TestUtils.REVERSE_LANDSCAPE)
    }

    /**
     * testSwitchingToReversePortrait, tests switching from Portrait Orientation to Reverse Portrait Orientation.
     */
    @Test
    fun testSwitchingToReversePortrait() {
        TestUtils.switchOrientation(editNoteActivityRule, TestUtils.PORTRAIT)
        TestUtils.switchOrientation(editNoteActivityRule, TestUtils.REVERSE_PORTRAIT)
    }
}