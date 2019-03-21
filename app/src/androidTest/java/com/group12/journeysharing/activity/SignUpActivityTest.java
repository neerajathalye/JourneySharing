package com.group12.journeysharing.activity;

import android.support.test.rule.ActivityTestRule;

import com.group12.journeysharing.R;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Neeraj Athalye on 12-Mar-19.
 */
public class SignUpActivityTest {

    @Rule
    public ActivityTestRule<SignUpActivity> activityTestRule = new ActivityTestRule<>(SignUpActivity.class);

    @Test
    public void invalidEmailPasswordTest() {
        onView(withId(R.id.firstNameEditText)).perform(typeText("amogh"));
        onView(withId(R.id.lastNameEditText)).perform(typeText("mbbs"));
        onView(withId(R.id.dobEditText)).perform(typeText("neerajathalye.1996@gmail.com"));
        onView(withId(R.id.phoneEditText)).perform(typeText("journey"));
        onView(withId(R.id.emailEditText)).perform(typeText("neerajathalye.1996@gmail.com"));
        onView(withId(R.id.passwordEditText)).perform(typeText("journey"));
        onView(withId(R.id.confirmPasswordEditText)).perform(typeText("neerajathalye.1996@gmail.com"));
        onView(withId(R.id.emergencyNameEditText)).perform(typeText("journey"));
        onView(withId(R.id.emergencyPhoneEditText)).perform(typeText("neerajathalye.1996@gmail.com"));
        onView(withId(R.id.emergencyEmailEditText)).perform(typeText("journey"));
        onView(withId(R.id.radioGroup)).perform(typeText("neerajathalye.1996@gmail.com"));
        onView(withId(R.id.countryCodePicker)).perform(typeText("journey"));
        onView(withId(R.id.genderTextView)).perform(typeText("neerajathalye.1996@gmail.com"));
        onView(withId(R.id.lastNameEditText)).perform(typeText("journey"));


        onView(withId(R.id.buttonSubmit)).perform(click());

        onView(withText("Invalid Email/Password")).inRoot(withDecorView(CoreMatchers.not((activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }


}