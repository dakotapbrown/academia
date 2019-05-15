package edu.vcu.eythirteenapp.Iteration01Tests;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalTime;

import edu.vcu.eythirteenapp.MainActivity;
import edu.vcu.eythirteenapp.R;
import edu.vcu.eythirteenapp.database.AlarmDatabaseHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;


/**************************************************************************************************
 * Instrumentation tests for the Project A13 app; this app was posthumously, but affectionately,  *
 * named the Force Awake(ns) Alarm App because we're all giant nerds (and I love Star Wars).      *
 *                                                                                                *
 * Note to instructor/grader:                                                                     *
 *      If the LocalTime of the emulator/device is set at a time where the hour is the same as    *
 *      the minute, these tests will unconditionally fail due to the inherent nature in which     *
 *      they are carried out. Since this only happens for 24 full minutes a day, and there are    *
 *      1,440 minutes in each day, we have elected to ignore this fault. Also, possibly because I *
 *      have no idea how to work around it. Your patience is appreciated                          *
 *                                                                                                *
 *                                                                                                *
 * @author Dakota Brown                                                                           *
 **************************************************************************************************/
@SuppressWarnings("CanBeFinal")
@LargeTest
@RunWith(AndroidJUnit4.class)
public class UserStory01Test {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule
            = new ActivityTestRule<>(MainActivity.class, false, false);


    /**********************************************************************************************
     * Launch the MainActivity before each test.                                                  *
     **********************************************************************************************/
    @Before
    public void setUp() {
        mMainActivityTestRule.launchActivity(new Intent());
    }


    /**********************************************************************************************
     * To easily remove all data from the SQL database after each test, simply delete the         *
     * database. A new one will be created when the onCreate() method of MainActivity is invoked  *
     * by the launching of the activity in the next setUp() method execution                       *
     **********************************************************************************************/
    @After
    public void cleanUp() {
        mMainActivityTestRule.getActivity().deleteDatabase(AlarmDatabaseHelper.TABLE_NAME);
        mMainActivityTestRule.finishActivity();
    }

    /**********************************************************************************************
     * This Espresso test is an acceptance test of User Story #1, Scenario #1.                    *
     *                                                                                            *
     *      User Story #1: As a user,                                                             *
     *                     so that I can wake up on time,                                         *
     *                     I want to be able to set multiple alarms                               *
     *      Scenario #1: Add new Alarm                                                            *
     *                                                                                            *
     *          Given I am on the app Main Activity,                                              *
     *          When I click Add New Alarm button,                                                *
     *          Then the Edit Alarm Activity should be displayed,                                 *
     *          And I can choose alarm settings,                                                  *
     *          When I click Save button,                                                         *
     *          Then I should be on Main Activity,                                                *
     *          And my alarm should be displayed.                                                 *
     *                                                                                            *
     **********************************************************************************************/
    @Test
    public void Scenario01() {
        /*----------------0-------------------------------------------------------------------------*
         * This Espresso test was recorded, but some code was refactored to allow for dynamic      *
         * testing. For instance, the EditText View that is displayed in the TimePickerDialog has  *
         * static text that depends on the LocalTime of the emulator/device at the moment of       *
         * creation of the AlarmEditActivity.                                                      *
         *-----------------------------------------------------------------------------------------*/


        // Get the LocalTime of machine for matching EditText views
        int hour = LocalTime.now().getHour();
        int minute = LocalTime.now().getMinute();
        if (hour > 12) {
            hour = hour - 12;
        }


        // Click AddAlarmButton from MainActivity, then TimeEditButton on AlarmEditActivity
        onView(allOf(withId(R.id.addAlarmButton), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.timeEditImageView), isDisplayed())).perform(click());


        // Switch TimePickerDialog to keyboard input and replace the EditTexts with strings equal to  the time "9:00"
        onView(
                allOf(
                        withContentDescription("Switch to text input mode for the time input."),
                        isDisplayed()))
                .perform(click());
        onView(
                allOf(
                        withClassName(is("android.widget.EditText")),
                        withText(String.valueOf(hour)),
                        isDisplayed()))
                .perform(replaceText("9"));
        onView(
                allOf(
                        withClassName(is("android.widget.EditText")),
                        withText(String.valueOf(minute)),
                        isDisplayed()))
                .perform(replaceText("00"));


        // Click OKButton on TimePickerDialog to return time to AlarmEditActivity, then click SaveButton to return to
        // MainActivity
        onView(allOf(withId(android.R.id.button1), withText("OK"), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.saveButton), isDisplayed())).perform(click());


        /*----------------------------------------------------------------------------------------*
         * Assertions:                                                                            *
         *      The RelativeLayout container holding the alarmTime TextView is displayed to the   *
         *          user                                                                          *
         *      The alarmTime TextView is displayed to the user                                   *
         *      The alarmTime text displayed in the TextView matches that of the set alarm        *
         *----------------------------------------------------------------------------------------*/
        // Assert the RelativeLayout that contains the Alarm info isDisplayed() in the ListView at
        // proper location. At this point, no other alarms should exist, so position should be 0.
        onView(childAtPosition(withId(R.id.mainActivityList), 0))
                .check(matches(isDisplayed()));


        // Assert the Alarm time isDisplayed() by checking for the R.id.alarmTime View
        onView(withId(R.id.alarmTime))
                .check(matches(isDisplayed()));


        // Assert the Alarm time matches the set alarm time
        onView(allOf(withId(R.id.alarmTime), isDisplayed()))
                .check(matches(withText("9:00 AM")));

    }


    /**********************************************************************************************
     * This Espresso test is an acceptance test of User Story #1, Scenario #2.                    *
     *                                                                                            *
     *      User Story #1: As a user,                                                             *
     *                     so that I can wake up on time,                                         *
     *                     I want to be able to set multiple alarms                               *
     *      Scenario #2: Add additional alarm                                                     *
     *                                                                                            *
     *          Given I am on the app Main Activity,                                              *
     *          And I have an existing alarm,                                                     *
     *          When I click Add New Alarm button,                                                *
     *          And I save my new alarm,                                                          *
     *          Then I should all see alarms on Main Activity.                                    *
     *                                                                                            *
     **********************************************************************************************/
    @Test
    public void Scenario02() {
        /*-----------------------------------------------------------------------------------------*
         * This Espresso test was copied from above code, then refactored to allow for testing of  *
         * multiple alarm creations.                                                               *
         *-----------------------------------------------------------------------------------------*/


        /*----------------------------------------------------------------------------------------*
         * Create first alarm. Time: 10:00AM                                                       *
         *----------------------------------------------------------------------------------------*/
        // Get the LocalTime of machine for matching EditText views
        int hour = LocalTime.now().getHour();
        int minute = LocalTime.now().getMinute();
        if (hour > 12) {
            hour = hour - 12;
        }


        // Click AddAlarmButton from MainActivity, then TimeEditButton on AlarmEditActivity
        onView(allOf(withId(R.id.addAlarmButton), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.timeEditImageView), isDisplayed())).perform(click());


        // Switch TimePickerDialog to keyboard input and replace the EditTexts with strings equal to  the time "9:00"
        onView(
                allOf(
                        withContentDescription("Switch to text input mode for the time input."),
                        isDisplayed()))
                .perform(click());
        onView(
                allOf(
                        withClassName(is("android.widget.EditText")),
                        withText(String.valueOf(hour)),
                        isDisplayed()))
                .perform(replaceText("10"));
        onView(
                allOf(
                        withClassName(is("android.widget.EditText")),
                        withText(String.valueOf(minute)),
                        isDisplayed()))
                .perform(replaceText("00"));


        // Click OKButton on TimePickerDialog to return time to AlarmEditActivity, then click SaveButton to return to
        // MainActivity
        onView(allOf(withId(android.R.id.button1), withText("OK"), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.saveButton), isDisplayed())).perform(click());


        /*----------------------------------------------------------------------------------------*
         * Create second alarm. Time: 9:00AM                                                      *
         *----------------------------------------------------------------------------------------*/
        // Get the LocalTime of machine for matching EditText views
        hour = LocalTime.now().getHour();
        minute = LocalTime.now().getMinute();
        if (hour > 12) {
            hour = hour - 12;
        }


        // Click AddAlarmButton from MainActivity, then TimeEditButton on AlarmEditActivity
        onView(allOf(withId(R.id.addAlarmButton), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.timeEditImageView), isDisplayed())).perform(click());


        // Switch TimePickerDialog to keyboard input and replace the EditTexts with strings equal to  the time "9:00"
        onView(
                allOf(
                        withContentDescription("Switch to text input mode for the time input."),
                        isDisplayed()))
                .perform(click());
        onView(
                allOf(
                        withClassName(is("android.widget.EditText")),
                        withText(String.valueOf(hour)),
                        isDisplayed()))
                .perform(replaceText("9"));
        onView(
                allOf(
                        withClassName(is("android.widget.EditText")),
                        withText(String.valueOf(minute)),
                        isDisplayed()))
                .perform(replaceText("00"));


        // Click OKButton on TimePickerDialog to return time to AlarmEditActivity,
        // then click SaveButton to return to MainActivity
        onView(allOf(withId(android.R.id.button1), withText("OK"), isDisplayed()))
                .perform(click());
        onView(allOf(withId(R.id.saveButton), isDisplayed()))
                .perform(click());


        /*----------------------------------------------------------------------------------------*
         * Assertions:                                                                            *
         *      RelativeLayouts containing alarm info exist and are displayed to the user         *
         *      The first alarm displayed is set to 9:00 AM                                       *
         *      The second alarm displayed is set to 10:00 AM                                     *
         *                                                                                        *
         * Note:                                                                                  *
         *      Positional indices are zero-based. So position of first alarm displayed is second *
         *      child of the first child of mainActivityList. In other words, the RelativeLayout  *
         *      containing alarmTime is the first child of the ListView, and alarmTime is the     *
         *      second child of the RelativeLayout.                                               *
         *----------------------------------------------------------------------------------------*/
        // Assert the RelativeLayouts containing the two Alarms just created isDisplayed() in the
        // ListView at proper location. Positions should be the reverse of the order they were added,
        // as Alarms are sorted by time.
        onView(childAtPosition(withId(R.id.mainActivityList), 0))
                .check(matches(isDisplayed()));
        onView(childAtPosition(withId(R.id.mainActivityList), 1))
                .check(matches(isDisplayed()));


        // Assert the first Alarm is 9:00 AM Alarm
        onView(
                allOf(withId(R.id.alarmTime),
                        childAtPosition(childAtPosition(withId(R.id.mainActivityList), 0), 1),
                        isDisplayed()))
                            .check(matches(withText("9:00 AM")));


        // Assert second Alarm is 10:00 AM Alarm
        onView(
                allOf(withId(R.id.alarmTime),
                        childAtPosition(
                                childAtPosition(withId(R.id.mainActivityList), 1), 1)
                        ))
                            .check(matches(allOf(isDisplayed(), withText("10:00 AM"))));
    }


    /**********************************************************************************************
     * This Espresso test is an acceptance test of User Story #1, Scenario #3.                    *
     *                                                                                            *
     *      User Story #1: As a user,                                                             *
     *                     so that I can wake up on time,                                         *
     *                     I want to be able to set multiple alarms                               *
     *      Scenario #3: Add new alarm that is the same as existing alarm                         *
     *                                                                                            *
     *          Given I have added an alarm,                                                      *
     *          When I edit an alarm that would be identical to an existing alarm,                *
     *          When I click the Save button,                                                     *
     *          Then I should be denied and told one already exists with those settings.          *
     *                                                                                            *
     **********************************************************************************************/
    @Test
    public void Scenario03() {
        /*-----------------------------------------------------------------------------------------*
         * This Espresso test was hand written to make it feel more like a unit test (and because  *
         * it's short!)                                                                            *
         *-----------------------------------------------------------------------------------------*/


        // Click the AddAlarmButton
        onView(allOf(withId(R.id.addAlarmButton), isDisplayed())).perform(click());


        // Grab the LocalTime for later use; save the alarm as is
        LocalTime firstAlarmTime = LocalTime.now();
        onView(allOf(withId(R.id.saveButton), isDisplayed())).perform(click());


        // Once again click AddAlarmButton, but this time grab the LocalTime again and set the alarm to
        // the first alarm's time
        onView(allOf(withId(R.id.addAlarmButton), isDisplayed())).perform(click());
        LocalTime secondAlarmTime = LocalTime.now();
        onView(allOf(withId(R.id.timeEditImageView), isDisplayed())).perform(click());


        // Switch TimePickerDialog to keyboard input and replace the EditTexts with strings equal to first
        // alarm's time
        onView(
                allOf(
                        withContentDescription("Switch to text input mode for the time input."),
                        isDisplayed()))
                .perform(click());
        onView(
                allOf(
                        withClassName(is("android.widget.EditText")),
                        withText(String.valueOf(secondAlarmTime.getHour())),
                        isDisplayed()))
                .perform(replaceText(String.valueOf(firstAlarmTime.getHour())));
        onView(
                allOf(
                        withClassName(is("android.widget.EditText")),
                        withText(String.valueOf(secondAlarmTime.getMinute())),
                        isDisplayed()))
                .perform(replaceText(String.valueOf(firstAlarmTime.getMinute())));
        onView(allOf(withId(android.R.id.button1), withText("OK"), isDisplayed()))
                .perform(click());


        // Click save to allow AlertDialog code to run
        onView(allOf(withId(R.id.saveButton), isDisplayed())).perform(click());


        /*----------------------------------------------------------------------------------------*
         * Assertions:                                                                            *
         *      The AlertDialog is displayed to the user                                          *
         *----------------------------------------------------------------------------------------*/
        onView(
                allOf(
                        withId(android.R.id.message),
                        withText(R.string.existingAlarmDialog)))
                .check(matches(isDisplayed()));
    }

    /**
     * Helper method generated by the Espresso Test Recorder.
     *
     * @param parentMatcher {@code Matcher<\View\>} that (potentially) has a child at {@code position}
     * @param position Position of potential child view to be matched (zero-based)
     * @return The {@code Matcher<\View\>} matching the child at {@code position}
     */
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
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

}
