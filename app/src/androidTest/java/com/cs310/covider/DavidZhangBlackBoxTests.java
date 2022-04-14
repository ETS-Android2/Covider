package com.cs310.covider;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.assertEquals;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DavidZhangBlackBoxTests {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private static void waitForElementUntilDisplayed(ViewInteraction element) {
        int i = 0;
        while (i++ < 200) {
            try {
                element.check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(1000);
                } catch (Exception e1) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void ClosePopup() {
        while (true) {
            try {
                onView(withText("CLOSE")).check(matches(isDisplayed())).perform(click());
            } catch (NoMatchingViewException e) {
                break;
            }
        }
    }

    public void EnsureLoggedOut() {
        try {
            Thread.sleep(1000);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        while (true) {
            try {
                ClosePopup();
                onView(withId(R.id.drawer_layout)).check(matches(isDisplayed())).perform(DrawerActions.open());
                onView(withId(R.id.menu_register_item)).check(matches(isDisplayed()));
                break;
            } catch (NoMatchingViewException e) {
                onView(withId(R.id.menu_logout_item)).check(matches(isDisplayed()))
                        .perform(click());
                onView(withText("YES")).check(matches(isDisplayed()))
                        .perform(click());
                waitForElementUntilDisplayed(onView(withId(R.id.login_email)));
            }
        }
    }

    @Test
    public void ensureLoginWorks() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(typeText("david_instructor@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("uscuscusc"), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ClosePopup();
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void testWrongEmailLogin() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(typeText("aaauscedu"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("uscuscusc"), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("CLOSE")).check(matches(isDisplayed()));
    }

    @Test
    public void testWrongPasswordLogin() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(typeText("david_instructor@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("aaaddd"), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("CLOSE")).check(matches(isDisplayed()));
    }

    @Test
    public void testIncorrectEmailRegister() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_register_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.register_email))
                .perform(typeText("david_instrucusc.edu"), closeSoftKeyboard());
        onView(withId(R.id.register_button))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.register_email)).check(matches(hasErrorText("Invalid Email!")));
    }

    @Test
    public void ensureCoursesPageWorks() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(typeText("david_instructor@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("uscuscusc"), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ClosePopup();
        onView(withId(R.id.map)).check(matches(isDisplayed()));
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_courses_item)).check(matches(isDisplayed())).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.courses_main_list)).check(matches(isDisplayed()));
    }
}