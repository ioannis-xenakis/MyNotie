package com.example.fanatic_coder.mynotepad;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
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

    @Test
    public void checkingIfClearTextButtonWorks() throws InterruptedException {
        onView(withId(R.id.noteTitle)).perform(typeText("Lorem Ipsum")).perform(closeSoftKeyboard());
        Thread.sleep(250);
        onView(withId(R.id.noteBodyText)).perform(typeText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce rhoncus tristique libero, in scelerisque nulla fermentum nec. Nam vel auctor enim. Donec eu purus molestie, fermentum libero eget, convallis purus. In eget fermentum ligula, nec porttitor tortor. Nullam blandit sapien nec ex sodales aliquet. Nunc massa urna, pharetra quis blandit at, vestibulum quis neque. Sed efficitur arcu tellus, a suscipit metus congue id. Duis interdum arcu sit amet suscipit scelerisque. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae;\n" +
                "\n" +
                "Morbi eu tortor a nisi ultricies fermentum. Sed eu egestas justo, a vestibulum arcu. Mauris vitae lorem nibh. Proin ultrices ex sit amet mauris porttitor, varius molestie neque venenatis. In eleifend, massa in gravida mattis, neque dui egestas neque, ut dapibus orci tellus sit amet justo. Suspendisse tincidunt volutpat nulla, id vestibulum erat. Sed id mauris metus. Ut faucibus cursus magna, convallis imperdiet tortor tristique a. Mauris sapien odio, elementum vulputate mauris sed, feugiat vestibulum augue. Morbi volutpat nisl in est facilisis tristique sit amet ac augue. Vivamus non justo in felis malesuada porta. Vestibulum interdum varius semper. Nam lobortis iaculis augue, id semper tellus congue et. Nunc tincidunt euismod arcu a volutpat. Maecenas vel dui at nisl blandit malesuada ut a sem.")).perform(closeSoftKeyboard());
        Thread.sleep(250);
        onView(withId(R.id.clearText)).perform(click());
        onView(withId(R.id.noteTitle)).check(matches(withText("")));
    }
}
