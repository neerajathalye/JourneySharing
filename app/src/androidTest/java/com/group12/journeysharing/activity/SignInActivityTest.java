package com.group12.journeysharing.activity;

import android.support.test.rule.ActivityTestRule;

import com.group12.journeysharing.R;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Neeraj Athalye on 12-Mar-19.
 */
public class SignInActivityTest {

    @Rule
    public ActivityTestRule<SignInActivity> activityTestRule = new ActivityTestRule<>(SignInActivity.class);

    @Test
    public void emptyEmailTest() {
//        onView(withId(R.id.emailField)).perform(typeText("neerajathalye.1996@gmail.com"), closeSoftKeyboard()).check(matches(withText("neerajathalye.1996@gmail.com")));
//        onView(withId(R.id.emailField)).perform(typeText("neerajathalye.1996@gmail.com"));
        onView(withId(R.id.emailField)).perform(typeText(""));
//        onView(withId(R.id.passwordField)).perform(typeText("journey123"));
        onView(withId(R.id.buttonSignin)).perform(click());

        onView(withText("Please enter email")).inRoot(withDecorView(CoreMatchers.not((activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void emptyPasswordTest() {
        onView(withId(R.id.emailField)).perform(typeText("neerajathalye.1996@gmail.com"));
        onView(withId(R.id.passwordField)).perform(typeText(""));
        onView(withId(R.id.buttonSignin)).perform(click());
        onView(withText("Please enter password")).inRoot(withDecorView(CoreMatchers.not((activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void invalidEmailPasswordTest() {
        onView(withId(R.id.emailField)).perform(typeText("neerajathalye.1996@gmail.com"));
        onView(withId(R.id.passwordField)).perform(typeText("journey"));
        onView(withId(R.id.buttonSignin)).perform(click());
        onView(withText("Invalid Email/Password")).inRoot(withDecorView(CoreMatchers.not((activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }


}