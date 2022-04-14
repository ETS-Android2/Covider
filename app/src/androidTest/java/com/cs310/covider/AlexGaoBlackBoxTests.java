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

@RunWith(AndroidJUnit4.class)
public class AlexGaoBlackBoxTests {
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
    public void studentCanOpenForm() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(typeText("student@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("1234567890"), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ClosePopup();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_form_item)).check(matches(isDisplayed())).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // onView(withId(R.id.symptoms_text)).check(matches(isDisplayed()));
        onView(withId(R.id.form_symptoms_selection)).check(matches(isDisplayed()));
        // onView(withId(R.id.test_text)).check(matches(isDisplayed()));
        onView(withId(R.id.form_test_selection)).check(matches(isDisplayed()));
        onView(withId(R.id.form_checkbox_agree)).check(matches(isDisplayed()));
        onView(withId(R.id.form_button)).check(matches(isDisplayed()));
    }

    @Test
    public void instructorCanOpenForm() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(typeText("alex_instructor@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("1234567890"), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ClosePopup();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_form_item)).check(matches(isDisplayed())).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // onView(withId(R.id.symptoms_text)).check(matches(isDisplayed()));
        onView(withId(R.id.form_symptoms_selection)).check(matches(isDisplayed()));
        // onView(withId(R.id.test_text)).check(matches(isDisplayed()));
        onView(withId(R.id.form_test_selection)).check(matches(isDisplayed()));
        onView(withId(R.id.form_checkbox_agree)).check(matches(isDisplayed()));
        onView(withId(R.id.form_button)).check(matches(isDisplayed()));
    }

    @Test
    public void studentCanSubmitForm() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(typeText("student@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("1234567890"), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ClosePopup();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_form_item)).check(matches(isDisplayed())).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.form_checkbox_agree)).perform(click());
        onView(withId(R.id.form_button)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("YES")).check(matches(isDisplayed())).perform(click());
        ClosePopup();
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void instructorCanSubmitForm() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(typeText("alex_instructor@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("1234567890"), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ClosePopup();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_form_item)).check(matches(isDisplayed())).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.form_checkbox_agree)).perform(click());
        onView(withId(R.id.form_button)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("YES")).check(matches(isDisplayed())).perform(click());
        ClosePopup();
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void userCanSubmitBuildingCheckinForm() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(typeText("student@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("1234567890"), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ClosePopup();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_checkin_item)).check(matches(isDisplayed())).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.checkin_submit_button)).perform(click());
        onView(withText("YES")).check(matches(isDisplayed())).perform(click());
        ClosePopup();
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }
}
