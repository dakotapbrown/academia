package edu.vcu.eythirteenapp.Iteration03Tests;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.vcu.eythirteenapp.AlarmWakeActivity;
import edu.vcu.eythirteenapp.R;
import edu.vcu.eythirteenapp.alarm.Alarm;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;


@SuppressWarnings("CanBeFinal")
@MediumTest
@RunWith(AndroidJUnit4.class)
public class UserStory02Test {
	
	private static Intent intent;
	private static Alarm alarm;
	
	@Rule
	public ActivityTestRule<AlarmWakeActivity> alarmWakeActivityTestRule
			= new ActivityTestRule<>(AlarmWakeActivity.class, false, false);
	
	
	@Test
	public void Scenario01() {
		intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putParcelable("alarm", alarm);
		intent.putExtra("alarm", bundle);
		
		alarmWakeActivityTestRule.launchActivity(intent);
		
		onView(withId(R.id.snoozeButton)).check(matches(isClickable()));
		onView(withId(R.id.dismissButton)).check(matches(isClickable()));
	}
	
	
	@Test
	public void Scenario02() {
		alarm.setSnoozeCount((byte) 3);
		
		intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putParcelable("alarm", alarm);
		intent.putExtra("alarm", bundle);
		
		alarmWakeActivityTestRule.launchActivity(intent);
		
		onView(withId(R.id.snoozeButton)).check(matches(not(isClickable())));
	}
	
	
	@Test
	public void Scenario03() {
		alarm.setSnoozeEnabled(false);
		
		intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putParcelable("alarm", alarm);
		intent.putExtra("alarm", bundle);
		
		alarmWakeActivityTestRule.launchActivity(intent);
		
		onView(withId(R.id.snoozeButton)).check(matches(not(isDisplayed())));
	}
	
	
	@After
	public void cleanUp() {
		alarmWakeActivityTestRule.finishActivity();
	}
	
	
	@BeforeClass
	public static void setUpClass() {
		alarm = new Alarm();
		alarm.setSnoozeEnabled(true);
	}
	
}
