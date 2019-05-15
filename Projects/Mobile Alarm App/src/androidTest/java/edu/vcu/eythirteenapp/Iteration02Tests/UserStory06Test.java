package edu.vcu.eythirteenapp.Iteration02Tests;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.SeekBar;

import java.time.LocalTime;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@SuppressWarnings({"WeakerAccess", "CanBeFinal", "EmptyMethod", "unused"})
@LargeTest
@RunWith(AndroidJUnit4.class)
public class UserStory06Test {
	
	private static Intent intent01;
	private static Intent intent02;
	private static Alarm alarm;
	public final ActivityTestRule<MainActivity> mainActivityRule
			= new ActivityTestRule<>(MainActivity.class, false, false);
	@Rule
	public ActivityTestRule<AlarmEditActivity> alarmEditActivityRule
			= new ActivityTestRule<>(AlarmEditActivity.class, false, false);
	
	
	@Test
	public void Scenario01() {
		alarmEditActivityRule.launchActivity(intent01);
		onView(withId(R.id.saveButton)).perform(click());
		
		alarmEditActivityRule.launchActivity(intent02);
		onView(withId(R.id.saveButton)).perform(click());
		
		//todo: need to find some way to check the values of each seekbar on the two alarm objects
		//and assert that they are different.
	}
	
	
	@Test
	public void Scenario02() {
		//todo: will be implemented once alarms have been implemented to play sound
	}
	
	
	@Test
	public void Scenario03() {
		//todo: will be implemented once alarms have been implemented to play sound.
	}
	
	
	@After
	public void cleanUp() {
		alarmEditActivityRule.finishActivity();
		
		mainActivityRule.getActivity().deleteDatabase(AlarmDatabaseHelper.TABLE_NAME);
		mainActivityRule.finishActivity();
	}
	
	
	public static ViewAction setProgress(final int progress) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return ViewMatchers.isAssignableFrom(SeekBar.class);
			}
			
			
			@Override
			public String getDescription() {
				return "Set a progress on a SeekBar";
			}
			
			
			@Override
			public void perform(UiController uiController, View view) {
				SeekBar seekBar = (SeekBar) view;
				seekBar.setProgress(progress);
			}
		};
	}
	
	
	@Before
	public void setUp() {
		mainActivityRule.launchActivity(new Intent());
	}
	
	
	@BeforeClass
	public static void setUpClass() {
		Alarm alarm01 = new Alarm();
		alarm01.setTime(LocalTime.of(4, 30));
		alarm01.setVolume((byte) 50);
		
		Alarm alarm02 = new Alarm();
		alarm02.setTime(LocalTime.of(17, 0));
		alarm02.setVolume((byte) 75);
		
		Bundle bundle01 = new Bundle();
		bundle01.putParcelable("alarm", alarm01);
		intent01 = new Intent();
		intent01.putExtra("alarm", bundle01);
		
		Bundle bundle02 = new Bundle();
		bundle02.putParcelable("alarm", alarm02);
		intent02 = new Intent();
		intent02.putExtra("alarm", bundle02);
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
						       && view.equals(((ViewGroup) parent).getChildAt(position));
			}
		};
	}
	
}
