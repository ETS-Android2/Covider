package com.cs310.covider;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.content.Context;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private static boolean waitForElementUntilDisplayed(ViewInteraction element) {
        int i = 0;
        while (i++ < 200) {
            try {
                element.check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(1000);
                } catch (Exception e1) {
                    e.printStackTrace();
                }
            }
        }
        return false;
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
    public void ensureTextChangesWork() {
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
}