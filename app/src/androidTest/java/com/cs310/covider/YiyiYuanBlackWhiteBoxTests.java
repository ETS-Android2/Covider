package com.cs310.covider;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.core.internal.deps.guava.base.Predicate;
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
import androidx.test.espresso.core.internal.deps.guava.collect.Lists;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class YiyiYuanBlackWhiteBoxTests {
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

    protected static void isVisible(int id) {
        onView(withId(id)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    protected static void isGone(int id) {
        onView(withId(id)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    private static Matcher<View> LinearLayoutCount(final int size) {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(final View view) {
                return ((LinearLayout) view).getChildCount() == size;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("Test Failed: Expected " + size + " items");
            }
        };
    }

//    private static ViewAction clickOnNonDisplayView = new ViewAction() {
//        @Override
//        public Matcher<View> getConstraints() {
//            return ViewMatchers.isEnabled();
//        }
//
//        @Override
//        public String getDescription() {
//            return "";
//        }
//
//        @Override
//        public void perform(UiController uiController, View view) {
//            view.performClick();
//        }
//    };

    private static Predicate<View> MatcherPred(final Matcher<View> matcher) {
        return matcher::matches;
    }

    protected static Matcher<View> ViewCount(final Matcher<View> viewMatcher, final int expectedCount) {
        return new TypeSafeMatcher<View>() {
            int actualCount = -1;

            @Override
            public void describeTo(Description description) {
                if (0 <= actualCount) {
                    description.appendText(String.valueOf(expectedCount));
                    description.appendText("\n With matcher: ");
                    viewMatcher.describeTo(description);
                    description.appendText(String.valueOf(actualCount));
                }
            }

            @Override
            protected boolean matchesSafely(View root) {
                actualCount = 0;
                Iterable<View> iterable = TreeIterables.breadthFirstViewTraversal(root);
                actualCount = Lists.newArrayList(Iterables.filter(iterable, MatcherPred(viewMatcher))).size();
                return actualCount == expectedCount;
            }
        };
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

    public void ClosePopup() {
        while (true) {
            try {
                onView(withText("CLOSE")).check(matches(isDisplayed())).perform(click());
            } catch (NoMatchingViewException e) {
                break;
            }
        }
    }

    @Test
    public void DefaultListDisplay() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(replaceText("hello2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(replaceText("yiyi11325"), closeSoftKeyboard());
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
        onView(withId(R.id.menu_building_item)).check(matches(isDisplayed())).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isGone(R.id.daily_schedule);
        isVisible(R.id.all_usc_buildings);
    }

    @Test
    public void ListDisplayAfterCoursesAdded() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(replaceText("hello@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(replaceText("yiyi11325"), closeSoftKeyboard());
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
        onView(withId(R.id.menu_building_item)).check(matches(isDisplayed())).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isVisible(R.id.list);
        isVisible(R.id.daily_schedule);
        isVisible(R.id.all_usc_buildings);
        onView(withId(R.id.list)).check(matches(isDisplayed()));
        onView(withId(R.id.daily_schedule)).check(matches(withText("Buildings in My Daily Schedule")));
        onView(withId(R.id.buildings_in_daily_schedule)).check(matches(LinearLayoutCount(3)));
        onView(isRoot()).check(matches(ViewCount(withText(R.string.acb_comp), 3)));
        onView(isRoot()).check(matches(ViewCount(withText(R.string.acc_comp), 3)));
        onView(isRoot()).check(matches(ViewCount(withText(R.string.ahf2_comp), 2)));
        onView(withId(R.id.all_usc_buildings)).check(matches(withText("All USC Buildings")));
    }

    @Test
    public void DefaultMapDisplay() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(replaceText("hello2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(replaceText("yiyi11325"), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ClosePopup();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_map_item)).check(matches(isDisplayed())).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void BuildingDetailDefaultDisplayInMapTab() throws UiObjectNotFoundException {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(replaceText("hello2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(replaceText("yiyi11325"), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ClosePopup();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_map_item)).check(matches(isDisplayed())).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("ann"));
        marker.exists();
        marker.click();
        onView(withId(R.id.building_comp)).check(matches(withText(R.string.ann_comp)));
        onView(withId(R.id.ratingBar)).noActivity();
        onView(withId(R.id.req)).check(matches(withText("Buy and Wear a N95 Mask")));
        onView(withId(R.id.ways)).check(matches(withText("Go to the USC Pharmacy or a Nearby CVS/Walgreens Store to Purchase")));
        onView(withId(R.id.return_to_previous)).perform(click());
//        UiObject marker2 = device.findObject(new UiSelector().descriptionContains("acc"));
//        marker2.exists();
//        marker2.click();
//        onView(withId(R.id.building_comp)).check(matches(withText(R.string.acc_comp)));
//        onView(withId(R.id.ratingBar)).noActivity();
//        onView(withId(R.id.req)).check(matches(withText("Show a Negative COVID Test")));
//        onView(withId(R.id.ways)).check(matches(withText("Testing on Campus: Secure an Appointment in MySHR in Advance or Use Walk-up Testing At the Sites")));
//        onView(withId(R.id.return_to_previous)).perform(click());
        UiObject marker3 = device.findObject(new UiSelector().descriptionContains("mcc"));
        marker3.exists();
        marker3.click();
        onView(withId(R.id.building_comp)).check(matches(withText(R.string.mcc_comp)));
        onView(withId(R.id.ratingBar)).noActivity();
        onView(withId(R.id.req)).check(matches(withText("Buy and Wear a Mask")));
        onView(withId(R.id.ways)).check(matches(withText("Masks Can Be Purchased at USC bookstore ($2/each) or Target")));
        onView(withId(R.id.return_to_previous)).perform(click());
    }

    @Test
    public void BuildingDetailDisplayInBuildingTab() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(replaceText("hello2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(replaceText("yiyi11325"), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ClosePopup();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_building_item)).check(matches(isDisplayed())).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.acc)).check(matches(isDisplayed())).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.building_comp)).check(matches(withText(R.string.acc_comp)));
        onView(withId(R.id.ratingBar)).noActivity();
        onView(withId(R.id.req)).check(matches(withText("Show a Negative COVID Test")));
        onView(withId(R.id.ways)).check(matches(withText("Testing on Campus: Secure an Appointment in MySHR in Advance or Use Walk-up Testing At the Sites")));
        onView(withId(R.id.return_to_previous)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.acb)).check(matches(isDisplayed())).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.building_comp)).check(matches(withText(R.string.acb_comp)));
        onView(withId(R.id.ratingBar)).noActivity();
        onView(withId(R.id.req)).check(matches(withText("Buy and Wear a Mask")));
        onView(withId(R.id.ways)).check(matches(withText("Masks Can Be Purchased at USC bookstore ($2/each) or Target")));
        onView(withId(R.id.return_to_previous)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        onView(withId(R.id.ann)).check(matches(isDisplayed())).perform(click());
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        onView(withId(R.id.building_comp)).check(matches(withText(R.string.ann_comp)));
//        onView(withId(R.id.ratingBar)).noActivity();
//        onView(withId(R.id.req)).check(matches(withText("Buy and Wear a N95 Mask")));
//        onView(withId(R.id.ways)).check(matches(withText("Go to the USC Pharmacy or a Nearby CVS/Walgreens Store to Purchase")));
//        onView(withId(R.id.return_to_previous)).perform(click());
    }

    public void DefaultRiskLevel() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(replaceText("hello2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(replaceText("yiyi11325"), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ClosePopup();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        ClosePopup();
        onView(withId(R.id.menu_building_item)).check(matches(isDisplayed())).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.building_comp)).check(matches(withText(R.string.acb_comp)));
        onView(withId(R.id.ratingBar)).check(matches(RatingMatcher(1.5F)));
    }

    @Test
    public void CheckFrequentlyVisitedSetUp1() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(replaceText("hello2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(replaceText("yiyi11325"), closeSoftKeyboard());
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
        // if already checked in, comment out line 431 - 438
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

        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(replaceText("hello2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(replaceText("yiyi11325"), closeSoftKeyboard());
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
        // if already checked in, comment out line 466 - 463
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
    public void CheckFrequentlyVisitedSetUp2() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(replaceText("hello@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(replaceText("yiyi11325"), closeSoftKeyboard());
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
        // if already checked in the building acb, comment out the following line (line 504)
        onView(withText("YES")).check(matches(isDisplayed())).perform(click());
        ClosePopup();

        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(replaceText("hello2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(replaceText("yiyi11325"), closeSoftKeyboard());
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
        // if already checked in the building acb, comment out the following line (line 531)
        onView(withText("YES")).check(matches(isDisplayed())).perform(click());
        ClosePopup();
    }

    @Test
    public void CheckFrequentlyVisitedInBuildingTap() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(replaceText("hello2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(replaceText("yiyi11325"), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ClosePopup();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        ClosePopup();
        onView(withId(R.id.menu_building_item)).check(matches(isDisplayed())).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isVisible(R.id.list);
        isVisible(R.id.buildings_frequently_visited);
        isVisible(R.id.all_usc_buildings);
        onView(withId(R.id.all_usc_buildings)).check(matches(withText("All USC Buildings")));
        onView(withId(R.id.list)).check(matches(isDisplayed()));
        onView(withId(R.id.frequent_visit)).check(matches(withText("Frequently Visited Buildings")));
        onView(isRoot()).check(matches(ViewCount(withText(R.string.acb_comp), 2)));
    }

    private static Matcher<View> RatingMatcher(final float rating) {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(final View view) {
                return ((RatingBar) view).getRating() == rating;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("Test Failed: Expected " + rating);
            }
        };
    }

    public void TestRiskLevelUpdate() {
        EnsureLoggedOut();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.menu_login_item)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_email))
                .perform(replaceText("hello2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(replaceText("yiyi11325"), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ClosePopup();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        ClosePopup();
        onView(withId(R.id.menu_building_item)).check(matches(isDisplayed())).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.building_comp)).check(matches(withText(R.string.acb_comp)));
        onView(withId(R.id.ratingBar)).check(matches(RatingMatcher((float) (1.5 + (.7 * 2 + .2 * 2 + .1 * 2)))));
    }
}