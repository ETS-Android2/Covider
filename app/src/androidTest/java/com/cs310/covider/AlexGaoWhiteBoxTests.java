package com.cs310.covider;

import com.cs310.covider.model.Building;
import com.cs310.covider.model.User;
import com.cs310.covider.model.Util;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.cs310.covider.model.Util.sameWeek;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.assertEquals;

import android.app.Activity;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cs310.covider.fragment.FormFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AlexGaoWhiteBoxTests {
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
    public void symptomsYes(){
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(typeText("student2@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("12345678"), closeSoftKeyboard());
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
        onView(withId(R.id.form_symptoms_selection)).perform(click());
        onData(hasToString(startsWith("Yes"))).perform(click());
        onView(withId(R.id.form_symptoms_selection)).check(matches(withSpinnerText(containsString("Yes"))));
    }

    @Test
    public void symptomsNo(){
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(typeText("student2@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("12345678"), closeSoftKeyboard());
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
        onView(withId(R.id.form_symptoms_selection)).perform(click());
        onData(hasToString(startsWith("No"))).perform(click());
        onView(withId(R.id.form_symptoms_selection)).check(matches(withSpinnerText(containsString("No"))));
    }

    @Test
    public void testPositive(){
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(typeText("student2@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("12345678"), closeSoftKeyboard());
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
        onView(withId(R.id.form_test_selection)).perform(click());
        onData(hasToString(startsWith("Positive"))).perform(click());
        onView(withId(R.id.form_test_selection)).check(matches(withSpinnerText(containsString("Positive"))));
    }

    @Test
    public void testNegative(){
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(typeText("student2@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("12345678"), closeSoftKeyboard());
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
        onView(withId(R.id.form_test_selection)).perform(click());
        onData(hasToString(startsWith("Negative"))).perform(click());
        onView(withId(R.id.form_test_selection)).check(matches(withSpinnerText(containsString("Negative"))));
    }

    @Test
    public void buildingCheckinFormBuildingName(){
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(typeText("student2@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("12345678"), closeSoftKeyboard());
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
        onView(withId(R.id.checkin_buildings_selection)).perform(click());
        onData(hasToString(startsWith("Ahmanson"))).perform(click());
        onView(withId(R.id.checkin_buildings_selection)).check(matches(withSpinnerText(containsString("ACB"))));

    }
}

