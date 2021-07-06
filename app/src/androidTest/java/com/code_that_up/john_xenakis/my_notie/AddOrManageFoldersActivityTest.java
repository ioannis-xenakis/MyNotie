package com.code_that_up.john_xenakis.my_notie;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

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
    public void testAddNewFolderEdittextExists() {
        assertNotNull(withId(R.id.add_new_folder_text_input_edittext));
        onView(withId(R.id.add_new_folder_text_input_edittext)).check(matches(isDisplayed()));
    }

    /**
     * testAddNewFolderEdittextWrites, tests by writing on Edittext.
     */
    @Test
    public void testAddNewFolderEdittextWrites() {
        onView(withId(R.id.add_new_folder_text_input_edittext)).perform(typeText("My folder 1"));
        onView(withId(R.id.add_new_folder_text_input_edittext)).check(matches(withText("My folder 1")));
    }

    /**
     * testClearAddNewFolderEdittext, tests by clearing text on Edittext.
     */
    @Test
    public void testClearAddNewFolderEdittext() {
        onView(withId(R.id.add_new_folder_text_input_edittext)).check(matches(withText("")));
        onView(withId(R.id.add_new_folder_text_input_edittext)).perform(typeText("My folder 1"));
        onView(withId(R.id.add_new_folder_text_input_edittext)).check(matches(withText("My folder 1")));
        onView(withContentDescription("Clear add new folder edittext text.")).perform(click());
        onView(withId(R.id.add_new_folder_text_input_edittext)).check(matches(withText("")));
    }
}