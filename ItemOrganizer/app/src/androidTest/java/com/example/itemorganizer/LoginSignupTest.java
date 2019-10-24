package com.example.itemorganizer;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
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
public class LoginSignupTest {

    @Rule
    public ActivityTestRule<AccountLogin> mActivityTestRule = new ActivityTestRule<>(AccountLogin.class);

    @Test
    public void loginSignupTest() {
        ViewInteraction editText = onView(
                allOf(withId(R.id.email_login),
                        isDisplayed()));
        editText.check(matches(isDisplayed()));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.password_login),
                        isDisplayed()));
        editText2.check(matches(isDisplayed()));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.imageView2),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.login_btn),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.newUser_text), withText("New User? Sign up!"),
                        isDisplayed()));
        textView.check(matches(withText("New User? Sign up!")));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.signup_btn),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.signup_btn), withText("Sign Up"),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.name_text), withText("Name"),
                        isDisplayed()));
        textView2.check(matches(withText("Name")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.email_text), withText("E-mail:"),
                        isDisplayed()));
        textView3.check(matches(withText("E-mail:")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.password_text), withText("Password:"),
                        isDisplayed()));
        textView4.check(matches(withText("Password:")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.textView3), withText("Confirm Password:"),
                        isDisplayed()));
        textView5.check(matches(withText("Confirm Password:")));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.signup_btn),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction imageView2 = onView(
                allOf(withId(R.id.imageView2),
                        isDisplayed()));
        imageView2.check(matches(isDisplayed()));
    }
}
