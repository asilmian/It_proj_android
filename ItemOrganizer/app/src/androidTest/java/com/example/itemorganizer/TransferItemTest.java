package com.example.itemorganizer;

import com.example.itemorganizer.Item.SingleItemView;
import com.example.itemorganizer.Item.TransferItem;

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
public class TransferItemTest {

    @Rule
    public ActivityTestRule<TransferItem> transferItemActivityTestRule = new ActivityTestRule<>(TransferItem.class);


    @Test
    public void transferItemTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.transferItemTransfer), withText("Transfer Item"),
                        isDisplayed()));
        appCompatButton.check(matches(withText("Transfer Item")));


        ViewInteraction button2 = onView(
                allOf(withId(R.id.textView11), withText("New Family"),
                        isDisplayed()));
        button2.check(matches(withText("New Family")));


    }


}
