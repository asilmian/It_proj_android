package com.example.itemorganizer;


import com.example.itemorganizer.AddItem.AddItemActivity;
import com.example.itemorganizer.Family.SingleFamilyView;

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
public class SingleFamilyViewTest {

    @Rule
    public ActivityTestRule<SingleFamilyView> mActivityTestRule = new ActivityTestRule<>(SingleFamilyView.class);

    @Test
    public void singleFamilyViewTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.textView6),
                        isDisplayed()));
        appCompatButton.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.textView9), withText("Family Members:"),
                        isDisplayed()));
        button.check(matches(withText("Family Members:")));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.single_family_view_leave), withText("Leave Family"),
                        isDisplayed()));
        button2.check(matches(withText("Leave Family")));

        ViewInteraction textView = onView(
                allOf(withId(R.id.single_family_view_current), withText("Set as Current"),
                        isDisplayed()));
        textView.check(matches(withText("Set as Current")));

        ViewInteraction iteminfo = onView(
                allOf(withId(R.id.textView), withText("Invite Code:"),
                        isDisplayed()));
        iteminfo.check(matches(withText("Invite Code:")));
    }

}
