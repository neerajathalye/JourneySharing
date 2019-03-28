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
public class SignInActivityTest2 {

    @Rule
    public ActivityTestRule<SignInActivity> mActivityTestRule = new ActivityTestRule<>(SignInActivity.class);

    @Test
    public void signInActivityTest2() {
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
        
        ViewInteraction appCompatButton2 = onView(
allOf(withId(R.id.buttonRegister), withText("Sign Up"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
0),
isDisplayed()));
        appCompatButton2.perform(click());
        
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
        appCompatEditText.perform(scrollTo(), replaceText("max"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText2 = onView(
allOf(withId(R.id.lastNameEditText),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
1)));
        appCompatEditText2.perform(scrollTo(), replaceText("qui"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText3 = onView(
allOf(withId(R.id.dobEditText),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
2)));
        appCompatEditText3.perform(scrollTo(), click());
        
        ViewInteraction appCompatButton3 = onView(
allOf(withId(android.R.id.button1), withText("OK"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
3)));
        appCompatButton3.perform(scrollTo(), click());
        
        ViewInteraction appCompatRadioButton = onView(
allOf(withId(R.id.oRadioButton), withText("I do not wish to specify"),
childAtPosition(
allOf(withId(R.id.radioGroup),
childAtPosition(
withClassName(is("android.support.constraint.ConstraintLayout")),
4)),
2)));
        appCompatRadioButton.perform(scrollTo(), click());
        
        ViewInteraction relativeLayout = onView(
allOf(withId(R.id.rlClickConsumer),
childAtPosition(
allOf(withId(R.id.countryCodeHolder),
childAtPosition(
withId(R.id.countryCodePicker),
0)),
0)));
        relativeLayout.perform(scrollTo(), click());
        
        ViewInteraction appCompatEditText4 = onView(
allOf(withId(R.id.editText_search),
childAtPosition(
allOf(withId(R.id.rl_query_holder),
childAtPosition(
withId(R.id.rl_holder),
1)),
0),
isDisplayed()));
        appCompatEditText4.perform(replaceText("ire"), closeSoftKeyboard());
        
        ViewInteraction recyclerView = onView(
allOf(withId(R.id.recycler_countryDialog),
childAtPosition(
withId(R.id.rl_holder),
2)));
        recyclerView.perform(actionOnItemAtPosition(1, click()));
        
        ViewInteraction appCompatEditText5 = onView(
allOf(withId(R.id.phoneEditText),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
6)));
        appCompatEditText5.perform(scrollTo(), replaceText("899475367"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText6 = onView(
allOf(withId(R.id.phoneEditText), withText("899475367"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
6)));
        appCompatEditText6.perform(scrollTo(), click());
        
        ViewInteraction appCompatEditText7 = onView(
allOf(withId(R.id.phoneEditText), withText("899475367"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
6)));
        appCompatEditText7.perform(scrollTo(), replaceText("833564208"));
        
        ViewInteraction appCompatEditText8 = onView(
allOf(withId(R.id.phoneEditText), withText("833564208"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
6),
isDisplayed()));
        appCompatEditText8.perform(closeSoftKeyboard());
        
        ViewInteraction appCompatButton4 = onView(
allOf(withId(R.id.verifyButton), withText("Verify"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
7)));
        appCompatButton4.perform(scrollTo(), click());
        
        ViewInteraction appCompatButton5 = onView(
allOf(withId(R.id.buttonSubmit), withText("Submit"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
15)));
        appCompatButton5.perform(scrollTo(), click());
        
        ViewInteraction appCompatButton6 = onView(
allOf(withId(R.id.buttonSubmit), withText("Submit"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
15)));
        appCompatButton6.perform(scrollTo(), click());
        
        ViewInteraction appCompatEditText9 = onView(
allOf(withId(R.id.phoneEditText), withText("833564208"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
6)));
        appCompatEditText9.perform(scrollTo(), replaceText("899475367"));
        
        ViewInteraction appCompatEditText10 = onView(
allOf(withId(R.id.phoneEditText), withText("899475367"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
6),
isDisplayed()));
        appCompatEditText10.perform(closeSoftKeyboard());
        
        ViewInteraction appCompatButton7 = onView(
allOf(withId(R.id.verifyButton), withText("Verify"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
7)));
        appCompatButton7.perform(scrollTo(), click());
        
        ViewInteraction appCompatButton8 = onView(
allOf(withId(R.id.verifyButton), withText("Verify"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
7)));
        appCompatButton8.perform(scrollTo(), click());
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
