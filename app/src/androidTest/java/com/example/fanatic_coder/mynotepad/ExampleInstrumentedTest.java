package com.example.fanatic_coder.mynotepad;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.fanatic_coder.mynotepad", appContext.getPackageName());
    }

    @Test
    public void userEntersNoteTitle() {
        onView(withId(R.id.noteTitle)).perform(typeText("Shopping List"));
    }

    @Test
    public void userEntersBodyText() {
        onView(withId(R.id.noteBodyText)).perform(typeText("Tomatoes Potatoes Cucumbers Potato Chips Bananas"));
    }

    @Test
    public void userEntersBodyTextAndNoteTitle() throws InterruptedException {
        onView(withId(R.id.noteTitle)).perform(typeText("Hello Note")).perform(closeSoftKeyboard());
        Thread.sleep(250);
        onView(withId(R.id.noteBodyText)).perform(typeText("Hey Dude"));
    }
}
