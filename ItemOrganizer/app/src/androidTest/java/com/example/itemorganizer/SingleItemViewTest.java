package com.example.itemorganizer;

import com.example.itemorganizer.AddItem.AddItemActivity;
import com.example.itemorganizer.Item.SingleItemView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SingleItemViewTest {

    @Rule
    public ActivityTestRule<SingleItemView> mActivityTestRule = new ActivityTestRule<>(SingleItemView.class);


    @Test
    public void singleItemTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.singleItemViewImage),
                        isDisplayed()));
        appCompatButton.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.imageView6),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction textDesc = onView(
                allOf(withId(R.id.singleItemViewDescription),
                        isDisplayed()));
        textDesc.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.singleItemViewDelete), withText("Delete"),
                        isDisplayed()));
        button2.check(matches(withText("Delete")));

        ViewInteraction textView = onView(
                allOf(withId(R.id.singleItemViewcp), withText("Edit Item"),
                        isDisplayed()));
        textView.check(matches(withText("Edit Item")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.singleItemViewTransfer), withText("Transfer Item"),
                        isDisplayed()));
        textView4.check(matches(withText("Transfer Item")));
    }
}
