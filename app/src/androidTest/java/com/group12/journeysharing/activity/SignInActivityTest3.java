package com.group12.journeysharing.activity;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import com.group12.journeysharing.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SignInActivityTest3 {

    @Rule
    public ActivityTestRule<SignInActivity> mActivityTestRule = new ActivityTestRule<>(SignInActivity.class);

    @Test
    public void signInActivityTest3() {
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction appCompatButton = onView(
allOf(withId(R.id.buttonRegister), withText("Sign Up"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
0),
isDisplayed()));
        appCompatButton.perform(click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction appCompatEditText = onView(
allOf(withId(R.id.firstNameEditText),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
0)));
        appCompatEditText.perform(scrollTo(), replaceText("adi"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText2 = onView(
allOf(withId(R.id.lastNameEditText),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
1)));
        appCompatEditText2.perform(scrollTo(), replaceText("dubey"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText3 = onView(
allOf(withId(R.id.dobEditText),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
2)));
        appCompatEditText3.perform(scrollTo(), click());
        
        ViewInteraction appCompatButton2 = onView(
allOf(withId(android.R.id.button1), withText("OK"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
3)));
        appCompatButton2.perform(scrollTo(), click());
        
        ViewInteraction appCompatRadioButton = onView(
allOf(withId(R.id.mRadioButton), withText("M"),
childAtPosition(
allOf(withId(R.id.radioGroup),
childAtPosition(
withClassName(is("android.support.constraint.ConstraintLayout")),
4)),
0)));
        appCompatRadioButton.perform(scrollTo(), click());
        
        ViewInteraction appCompatRadioButton2 = onView(
allOf(withId(R.id.oRadioButton), withText("I do not wish to specify"),
childAtPosition(
allOf(withId(R.id.radioGroup),
childAtPosition(
withClassName(is("android.support.constraint.ConstraintLayout")),
4)),
2)));
        appCompatRadioButton2.perform(scrollTo(), click());
        
        ViewInteraction appCompatRadioButton3 = onView(
allOf(withId(R.id.fRadioButton), withText("F"),
childAtPosition(
allOf(withId(R.id.radioGroup),
childAtPosition(
withClassName(is("android.support.constraint.ConstraintLayout")),
4)),
1)));
        appCompatRadioButton3.perform(scrollTo(), click());
        
        ViewInteraction relativeLayout = onView(
allOf(withId(R.id.rlClickConsumer),
childAtPosition(
allOf(withId(R.id.countryCodeHolder),
childAtPosition(
withId(R.id.countryCodePicker),
0)),
0)));
        relativeLayout.perform(scrollTo(), click());
        
        ViewInteraction relativeLayout2 = onView(
allOf(withId(R.id.rlClickConsumer),
childAtPosition(
allOf(withId(R.id.countryCodeHolder),
childAtPosition(
withId(R.id.countryCodePicker),
0)),
0)));
        relativeLayout2.perform(scrollTo(), click());
        
        ViewInteraction appCompatEditText4 = onView(
allOf(withId(R.id.editText_search),
childAtPosition(
allOf(withId(R.id.rl_query_holder),
childAtPosition(
withId(R.id.rl_holder),
1)),
0),
isDisplayed()));
        appCompatEditText4.perform(replaceText("irel"), closeSoftKeyboard());
        
        ViewInteraction recyclerView = onView(
allOf(withId(R.id.recycler_countryDialog),
childAtPosition(
withId(R.id.rl_holder),
2)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));
        
        ViewInteraction recyclerView2 = onView(
allOf(withId(R.id.recycler_countryDialog),
childAtPosition(
withId(R.id.rl_holder),
2)));
        recyclerView2.perform(actionOnItemAtPosition(103, click()));
        
        ViewInteraction appCompatEditText5 = onView(
allOf(withId(R.id.phoneEditText),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
6)));
        appCompatEditText5.perform(scrollTo(), replaceText("899475367"), closeSoftKeyboard());
        
        ViewInteraction appCompatButton3 = onView(
allOf(withId(R.id.verifyButton), withText("Verify"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
7)));
        appCompatButton3.perform(scrollTo(), click());
        
        ViewInteraction appCompatEditText6 = onView(
allOf(withId(R.id.emailEditText),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
8)));
        appCompatEditText6.perform(scrollTo(), replaceText("adaditi4@gmail.com"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText7 = onView(
allOf(withId(R.id.passwordEditText),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
9)));
        appCompatEditText7.perform(scrollTo(), replaceText("1234567890"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText8 = onView(
allOf(withId(R.id.confirmPasswordEditText),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
10)));
        appCompatEditText8.perform(scrollTo(), replaceText("1234567890"), closeSoftKeyboard());
        
        ViewInteraction appCompatButton4 = onView(
allOf(withId(R.id.buttonSubmit), withText("Submit"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
15)));
        appCompatButton4.perform(scrollTo(), click());
        }
    
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
