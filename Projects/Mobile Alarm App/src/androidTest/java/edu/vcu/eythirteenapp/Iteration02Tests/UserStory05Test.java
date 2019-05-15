package edu.vcu.eythirteenapp.Iteration02Tests;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import java.time.LocalTime;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.vcu.eythirteenapp.AlarmEditActivity;
import edu.vcu.eythirteenapp.AlarmWakeActivity;
import edu.vcu.eythirteenapp.MainActivity;
import edu.vcu.eythirteenapp.R;
import edu.vcu.eythirteenapp.alarm.Alarm;
import edu.vcu.eythirteenapp.database.AlarmDatabaseHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
@LargeTest
@RunWith(AndroidJUnit4.class)
public class UserStory05Test {
	
	private static Intent intent;
	private static Alarm alarm;
	public final ActivityTestRule<AlarmWakeActivity> alarmWakeActivityTestRule
			= new ActivityTestRule<>(AlarmWakeActivity.class, false, false);
	public final ActivityTestRule<MainActivity> mainActivityRule
			= new ActivityTestRule<>(MainActivity.class, false, false);
	@Rule
	public ActivityTestRule<AlarmEditActivity> alarmEditActivityTestRule
			= new ActivityTestRule<>(AlarmEditActivity.class, false, false);
	
	
	@Test
	public void Scenario01() {
		alarmEditActivityTestRule.launchActivity(intent);
		
		onView(withId(R.id.easyCheckBox)).check(matches(allOf(isDisplayed(), isClickable())));
		onView(withId(R.id.mediumCheckBox)).check(matches(allOf(isDisplayed(), isClickable())));
		onView(withId(R.id.hardCheckBox)).check(matches(allOf(isDisplayed(), isClickable())));
	}
	
	
	@Test
	public void Scenario02() {
		alarmEditActivityTestRule.launchActivity(intent);
		
		onView(withId(R.id.hardCheckBox)).perform(click());
		onView(withId(R.id.saveButton)).perform(click());
		
		onView(withId(R.id.editButton)).perform(click());
		onView(withChild(withId(R.id.alarmTime))).perform(click());
		
		onView(withId(R.id.mediumCheckBox)).perform(click());
		
		onView(withId(R.id.mediumCheckBox)).check(matches(allOf(isDisplayed(), isClickable(), isChecked())));
		onView(withId(R.id.hardCheckBox)).check(matches(allOf(isDisplayed(), isClickable(), isNotChecked()
		                                                     )));
	}
	
	
	@Test
	public void Scenario03() {
		alarmEditActivityTestRule.launchActivity(intent);
		onView(withId(R.id.saveButton)).perform(click());
		
		alarm = new Alarm(1, alarm.getTime(), alarm.getLabel(), alarm.getRepeatDays());
		intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putParcelable("alarm", alarm);
		intent.putExtra("alarm", bundle);
		
		alarmWakeActivityTestRule.launchActivity(intent);
		onView(withId(R.id.snoozeButton)).check(matches(isDisplayed()));
		onView(withId(R.id.snoozeButton)).perform(click());
		
		onView(withId(R.id.editButton)).perform(click());
		onView(withChild(withId(R.id.alarmTime))).perform(click());
		
		onView(withId(R.id.mediumCheckBox)).check(matches(allOf(isDisplayed(), isClickable(), isChecked())));
	}
	
	
	@After
	public void cleanUp() {
		mainActivityRule.getActivity().deleteDatabase(AlarmDatabaseHelper.TABLE_NAME);
		mainActivityRule.finishActivity();
	}
	
	
	@Before
	public void setUp() {
		mainActivityRule.launchActivity(new Intent());
	}
	
	
	@BeforeClass
	public static void setUpClass() {
		alarm = new Alarm();
		alarm.setTime(LocalTime.of(13, 56));
		
		Bundle bundle = new Bundle();
		bundle.putParcelable("alarm", alarm);
		intent = new Intent();
		intent.putExtra("alarm", bundle);
	}
	
}
