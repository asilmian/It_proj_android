package com.example.itemorganizer;

import com.example.itemorganizer.Family.FamilyLogIn;

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
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddFamilyTest {

    @Rule
    public ActivityTestRule<FamilyLogIn> mActivityTestRule = new ActivityTestRule<>(FamilyLogIn.class);



    @Test
    public void addItemTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.new_family_view),
                        isDisplayed()));
        appCompatButton.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.textView2),
                        isDisplayed(), withText("Family Code:")));
        button.check(matches(withText("Family Code:")));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.familyToken),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.joinFamily), withText("Join Family"),
                        isDisplayed()));
        textView.check(matches(withText("Join Family")));

        ViewInteraction iteminfo = onView(
                allOf(withId(R.id.newFamily),
                        isDisplayed()));
        iteminfo.check(matches(withText("New Family")));
        iteminfo.perform(click());

        ViewInteraction newFam = onView(
                allOf(withId(R.id.new_family_view),
                        isDisplayed()));
        newFam.check(matches(isDisplayed()));

        ViewInteraction famCode = onView(
                allOf(withId(R.id.famNameView),
                isDisplayed()));
        famCode.check((matches(withText("Family Name:"))));

        ViewInteraction namefield = onView(
                allOf(withId(R.id.familyName),
                isDisplayed()));
        namefield.check(matches(isDisplayed()));

        ViewInteraction createButton = onView(
                allOf(withId(R.id.createFamily),
                        isDisplayed(),
                        withText("create family")));
        createButton.check(matches(withText("create family")));

    }
}
