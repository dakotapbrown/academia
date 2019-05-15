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
import edu.vcu.eythirteenapp.MainActivity;
import edu.vcu.eythirteenapp.R;
import edu.vcu.eythirteenapp.alarm.Alarm;
import edu.vcu.eythirteenapp.database.AlarmDatabaseHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
@LargeTest
@RunWith(AndroidJUnit4.class)
public class UserStory10Test {
	
	private static Intent intent01;
	private static Intent intent02;
	private static Intent intent03;
	private static Intent intent04;
	public final ActivityTestRule<MainActivity> mainActivityRule
			= new ActivityTestRule<>(MainActivity.class, false, false);
	@Rule
	public ActivityTestRule<AlarmEditActivity> alarmEditActivityRule
			= new ActivityTestRule<>(AlarmEditActivity.class, false, false);
	
	
	@Test
	public void Scenario01() {
		alarmEditActivityRule.launchActivity(intent01);
		
		onView(withId(R.id.labelEditImageView)).perform(click());
		
		onView(withText(R.string.labelEditDialog)).check(matches(isDisplayed()));
	}
	
	
	@Test
	public void Scenario02() {
		alarmEditActivityRule.launchActivity(intent01);
		onView(withId(R.id.saveButton)).perform(click());
		
		alarmEditActivityRule.launchActivity(intent02);
		onView(withId(R.id.saveButton)).perform(click());
		
		alarmEditActivityRule.launchActivity(intent03);
		onView(withId(R.id.saveButton)).perform(click());
		
		alarmEditActivityRule.launchActivity(intent04);
		onView(withId(R.id.saveButton)).perform(click());
		
		onView(withText("Alarm03 Test")).check(matches(isDisplayed()));
		onView(withText("Alarm04 Test")).check(matches(isDisplayed()));
	}
	
	
	@Test
	public void Scenario03() {
		alarmEditActivityRule.launchActivity(intent01);
		onView(withId(R.id.saveButton)).perform(click());
		
		onView(withText("Alarm")).check(matches(isDisplayed()));
	}
	
	
	@After
	public void cleanUp() {
		alarmEditActivityRule.finishActivity();
		
		mainActivityRule.getActivity().deleteDatabase(AlarmDatabaseHelper.TABLE_NAME);
		mainActivityRule.finishActivity();
	}
	
	
	@Before
	public void setUp() {
		mainActivityRule.launchActivity(new Intent());
	}
	
	
	@BeforeClass
	public static void setUpClass() {
		Alarm alarm01 = new Alarm();
		alarm01.setTime(LocalTime.of(4, 30));
		
		Alarm alarm02 = new Alarm();
		alarm02.setTime(LocalTime.of(17, 0));
		
		Alarm alarm03 = new Alarm();
		alarm03.setLabel("Alarm03 Test");
		alarm03.setTime(LocalTime.of(10, 45));
		
		Alarm alarm04 = new Alarm();
		alarm04.setLabel("Alarm04 Test");
		alarm04.setTime(LocalTime.of(6, 15));
		
		Bundle bundle01 = new Bundle();
		bundle01.putParcelable("alarm", alarm01);
		intent01 = new Intent();
		intent01.putExtra("alarm", bundle01);
		
		Bundle bundle02 = new Bundle();
		bundle02.putParcelable("alarm", alarm02);
		intent02 = new Intent();
		intent02.putExtra("alarm", bundle02);
		
		Bundle bundle03 = new Bundle();
		bundle03.putParcelable("alarm", alarm03);
		intent03 = new Intent();
		intent03.putExtra("alarm", bundle03);
		
		Bundle bundle04 = new Bundle();
		bundle04.putParcelable("alarm", alarm04);
		intent04 = new Intent();
		intent04.putExtra("alarm", bundle04);
	}
}
