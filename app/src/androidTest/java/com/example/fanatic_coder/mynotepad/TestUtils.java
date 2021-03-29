package com.example.fanatic_coder.mynotepad;

import android.content.pm.ActivityInfo;
import android.view.View;

import androidx.test.espresso.Root;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matcher;

import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;

/**
 * <h2>TestUtils</h2> is a class, which contains tools/utilities, for instrumentation tests.
 * @author John/Ioannis Xenakis
 * @version 1.0
 * @see com.example.fanatic_coder.mynotepad.MyNotesActivityTest The MyNotesActivity tests, uses TestUtils class.
 */
public class TestUtils {

    /**
     * Landscape screen orientation.
     */
    public static int LANDSCAPE = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

    /**
     * Portrait screen orientation.
     */
    public static int PORTRAIT = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    /**
     * Reverse landscape screen orientation.
     */
    public static int REVERSE_LANDSCAPE = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;

    /**
     * Reverse portrait screen orientation.
     */
    public static int REVERSE_PORTRAIT = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;

    /**
     * clickChildViewWithId, clicks on a child view(for ex. button), inside a parent(for ex. note),
     * with specifying child's view id(for ex. R.id.buttonname), to know which child view to click.
     * @param id child's id number(for ex. button) to specify the exact child view.
     * @return the clicking action on child view.
     */
    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on button";
            }

            /**
             * perform, handles the main clicking mechanism, when performing the clicking.
             * @param uiController the controller, which controls operations about ui action types(clicks, scrolls, swipes, etc.).
             * @param view the parent or note.
             */
            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

    /**
     * switchOrientation, switches the screen orientation(e.g. Landscape) of the device.
     */
    public static void switchOrientation(ActivityScenarioRule<?> rule, int orientation) {
        rule.getScenario().onActivity(activity -> activity.setRequestedOrientation(orientation));
    }

    /**
     * isPopupWindow, returns the popup window(if it exists) to act on it.
     * @return the popup window.
     */
    public static Matcher<Root> isPopupWindow() {
        return isPlatformPopup();
    }

}
