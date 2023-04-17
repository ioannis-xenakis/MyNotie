package com.code_that_up.john_xenakis.my_notie

import android.content.pm.ActivityInfo
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Root
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import kotlin.jvm.internal.Intrinsics

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
 * <h2>TestUtils</h2> is a class, which contains tools/utilities, for instrumentation tests.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see MyNotesActivityTest The MyNotesActivity tests, uses TestUtils class.
 */
object TestUtils {
    /**
     * Landscape screen orientation.
     */
    var LANDSCAPE = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    /**
     * Portrait screen orientation.
     */
    var PORTRAIT = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    /**
     * Reverse landscape screen orientation.
     */
    var REVERSE_LANDSCAPE = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE

    /**
     * Reverse portrait screen orientation.
     */
    var REVERSE_PORTRAIT = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT

    /**
     * clickChildViewWithId, clicks on a child view(for ex. button), inside a parent(for ex. note),
     * with specifying child's view id(for ex. R.id.buttonname), to know which child view to click.
     * @param id child's id number(for ex. button) to specify the exact child view.
     * @return the clicking action on child view.
     */
    fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            /**
             * Gets the description for the child view(for ex. button).
             * @return The child views description text.
             */
            override fun getDescription(): String {
                return "Click on button"
            }

            /**
             * perform, handles the main clicking mechanism, when performing the clicking.
             * @param uiController the controller, which controls operations about ui action types(clicks, scrolls, swipes, etc.).
             * @param view the parent or note.
             */
            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id)
                v.performClick()
            }
        }
    }

    /**
     * typeEdittextWithId, types on an Edittext, inside a parent(for ex. folder), with specifying the Edittext's id,
     * to know which Edittext to type in.
     * @param id The Edittext's id number for specifying the Edittext.
     * @param typedText The text that is typed in Edittext.
     * @return The typing action on Edittext.
     */
    fun typeEdittextWithId(id: Int, typedText: String?): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            /**
             * getDescription, gets the description of what this ViewAction does.
             * @return The description text.
             */
            override fun getDescription(): String {
                return "Type on Edittext"
            }

            /**
             * perform, handles the main typing mechanism/part when typing. It is the heart of *performing* the actual typing.
             * @param uiController The controller, which controls operations about ui action types(clicks, scrolls, swipes etc.).
             * @param view the parent of the edittext(for ex. a folder).
             */
            override fun perform(uiController: UiController, view: View) {
                val editText = view.findViewById<EditText>(id)
                editText.setText(typedText)
            }
        }
    }

    /**
     * atPosition, specifies the position, the item will be on a RecyclerView
     * and the item Matcher that will be given, in order to locate which item to be used.
     * @param position The position the item will be on RecyclerView.
     * @param itemMatcher The matcher that will be given in order for the item to be found.
     * @return The matcher that found the item.
     */
    fun atPosition(position: Int, itemMatcher: Matcher<View?>): BoundedMatcher<View?, RecyclerView> {
        Intrinsics.checkNotNull(itemMatcher)
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            /**
             * describeTo, gives a description about the item.
             * @param description The description of the item.
             */
            override fun describeTo(description: Description) {
                description.appendText("has item at position: $position")
                itemMatcher.describeTo(description)
            }

            /**
             * matchesSafely, has the main mechanism to match with an particular item on a RecyclerView.
             * @param view The item on RecyclerView.
             * @return The result of a found item on a RecyclerView.
             */
            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: //has no item on such position.
                    return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }

    /**
     * switchOrientation, switches the screen orientation(e.g. Landscape) of the device.
     */
    fun switchOrientation(rule: ActivityScenarioRule<*>, orientation: Int) {
        rule.scenario.onActivity { activity -> activity.requestedOrientation = orientation }
    }

    val isPopupWindow: Matcher<Root>
        /**
         * isPopupWindow, returns the popup window(if it exists) to act on it.
         * @return the popup window.
         */
        get() = RootMatchers.isPlatformPopup()
}