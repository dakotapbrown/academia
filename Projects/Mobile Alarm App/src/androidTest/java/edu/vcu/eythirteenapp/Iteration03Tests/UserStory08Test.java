package edu.vcu.eythirteenapp.Iteration03Tests;

import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiSelector;

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
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("CanBeFinal")
@LargeTest
@RunWith(AndroidJUnit4.class)
public class UserStory08Test {
	private static Alarm alarm;
	private static Intent intent;
	private static Bundle bundle;
	
	@Rule
	public ActivityTestRule<AlarmEditActivity> alarmEditActivityTestRule
			= new ActivityTestRule<>(AlarmEditActivity.class, false, false);
	
	@Rule
	public ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);
	
	private UiDevice device;
	
	
	@Test
	public void Scenario01() {
		RingtoneManager r = new RingtoneManager(main.getActivity().getApplicationContext());
		r.setType(RingtoneManager.TYPE_ALARM);
		Cursor c = r.getCursor();
		Uri tone = null;
		if (c != null) {
			c.moveToFirst();
			while (!r.getRingtone(c.getPosition())
					        .getTitle(main.getActivity().getApplicationContext())
					        .equals("Carbon")) {
				c.moveToNext();
			}
			
			tone = r.getRingtoneUri(c.getPosition());
		}
		alarm.setTone(tone);
		intent = new Intent();
		bundle = new Bundle();
		bundle.putParcelable("alarm", alarm);
		intent.putExtra("alarm", bundle);
		
		alarmEditActivityTestRule.launchActivity(intent);
		
		onView(withId(R.id.saveButton)).perform(click());
		
		while (true) {
			if (!main.getActivity().hasWindowFocus()) { break; }
		}
		
		LocalTime future = LocalTime.now().plusSeconds(5L);
		while (true) {
			if (!future.isAfter(LocalTime.now())) { break; }
		}
		
		AudioManager manager = main.getActivity()
				                       .getApplicationContext()
				                       .getSystemService(AudioManager.class);
		
		assertTrue(manager != null && manager.isMusicActive());
	}
	
	
	@Test
	public void Scenario02() {
		alarmEditActivityTestRule.launchActivity(intent);
		onView(withId(R.id.saveButton)).perform(click());
		
		while (true) {
			if (!main.getActivity().hasWindowFocus()) { break; }
		}
		
		LocalTime future = LocalTime.now().plusSeconds(5L);
		while (true) {
			if (!future.isAfter(LocalTime.now())) { break; }
		}
		
		AudioManager manager = alarmEditActivityTestRule.getActivity()
				                       .getApplicationContext()
				                       .getSystemService(AudioManager.class);
		
		assertTrue(manager != null && manager.isMusicActive());
	}
	
	
	@Test
	public void Scenario03() {
		Intents.init();
		
		alarmEditActivityTestRule.launchActivity(intent);
		onView(withId(R.id.toneEditButton)).perform(click());
		
		intended(hasAction(RingtoneManager.ACTION_RINGTONE_PICKER));
	}
	
	
	@BeforeClass
	public static void classSetUp() {
		alarm = new Alarm()
				        .setTime(
						        LocalTime.now()
								        .plusMinutes(1)
								        .withSecond(0)
								        .withNano(0))
				        .setVolume((byte) 0);
		
		intent = new Intent();
		bundle = new Bundle();
		bundle.putParcelable("alarm", alarm);
		intent.putExtra("alarm", bundle);
	}
	
	
	@After
	public void cleanUp() {
		
		main.getActivity().deleteDatabase(AlarmDatabaseHelper.DB_NAME);
		main.getActivity().deleteDatabase(AlarmDatabaseHelper.TABLE_NAME);
		
		try {
			device.findObject((new UiSelector()).text("OK")).click();
		} catch (Exception ignored) {}
		
		main.finishActivity();
	}
	
	
	@Before
	public void setUp() {
		device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
	}
	
}

