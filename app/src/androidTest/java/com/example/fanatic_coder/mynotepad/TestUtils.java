package com.example.fanatic_coder.mynotepad;

import android.view.View;

import androidx.test.espresso.Root;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
//TODO Add javadoc comments, in TestUtils.
public class TestUtils {

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

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

    public static Matcher<Root> isPopupWindow() {
        return isPlatformPopup();
    }

}
