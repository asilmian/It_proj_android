package com.example.itemorganizer;

import com.example.itemorganizer.AddItem.AddItemActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


/**
 *
 * Test for adding item activity
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddItemTest {

    @Rule
    public ActivityTestRule<AddItemActivity> mActivityTestRule = new ActivityTestRule<>(AddItemActivity.class);

    @Test
    public void addItemTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.PictureBtn),
                        isDisplayed()));
        appCompatButton.check(matches(withText("Take Picture")));

        ViewInteraction button = onView(
                allOf(withId(R.id.button3),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.button2),
                        isDisplayed()));
        button2.check(matches(withText("suggest tags")));

        ViewInteraction textView = onView(
                allOf(withId(R.id.submit_item_button), withText("Add Item"),
                        isDisplayed()));
        textView.check(matches(withText("Add Item")));

        ViewInteraction iteminfo = onView(
                allOf(withId(R.id.scrollView4),
                        isDisplayed()));
        iteminfo.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.new_family_view),
                        isDisplayed()));
        textView4.check(matches(isDisplayed()));
    }
}

