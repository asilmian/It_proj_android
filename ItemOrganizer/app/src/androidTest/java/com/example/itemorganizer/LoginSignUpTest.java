package com.example.itemorganizer;

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

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginSignUpTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void loginSignUpTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button), withText("Enter System"),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.login_btn),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.signup_btn),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.username_text), withText("E-mail:"),
                        isDisplayed()));
        textView.check(matches(withText("E-mail:")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.password_text), withText("Password:"),
                        isDisplayed()));
        textView2.check(matches(withText("Password:")));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.signup_btn), withText("Sign Up"),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.name_text), withText("Name"),
                        isDisplayed()));
        textView3.check(matches(withText("Name")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.email_text), withText("E-mail:"),
                        isDisplayed()));
        textView4.check(matches(withText("E-mail:")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.password_text), withText("Password:"),
                        isDisplayed()));
        textView5.check(matches(withText("Password:")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.textView3), withText("Confirm Password:"),
                        isDisplayed()));
        textView6.check(matches(withText("Confirm Password:")));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.signup_btn),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));
    }
}
