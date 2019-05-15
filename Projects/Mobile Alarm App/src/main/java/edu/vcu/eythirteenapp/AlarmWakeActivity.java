package edu.vcu.eythirteenapp;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import edu.vcu.eythirteenapp.alarm.Alarm;
import edu.vcu.eythirteenapp.alarm.AlarmFormatter;
import edu.vcu.eythirteenapp.database.AlarmDatabaseManager;
import edu.vcu.eythirteenapp.dialogs.DialogListener;
import edu.vcu.eythirteenapp.dialogs.MathQuestionDialogFragment;

/**
 * The alarm wake activity serves as the screen that is visible while an alarm is going off.
 * It has little functionality during iteration 1, as my main focus was on the random Math
 * equations.
 *
 * @author Matthew Meyer
 */

public class AlarmWakeActivity extends FragmentActivity implements DialogListener {
	
	private static final String TAG = "AlarmWakeActivity";
	private Alarm mAlarm;
	private AlarmDatabaseManager manager;
	private MediaPlayer player;
	private final static int MAX_VOLUME = 100;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_wake);
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("alarm");
		
		try {
			assert bundle != null;
			mAlarm = bundle.getParcelable("alarm");
			manager = new AlarmDatabaseManager(this);
			populateViews();
		} catch (AssertionError ae) {
			Log.e(TAG, "onCreate: No alarm info found.");
			Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show();
			finish();
		}
		
		try {
			player = MediaPlayer.create(this, mAlarm.getTone());
			float volume = (float) (1-(Math.log(MAX_VOLUME - mAlarm.getVolume()) / Math.log(MAX_VOLUME)));
			player.setVolume(volume, volume);
			player.setLooping(true);
			player.start();
		} catch (NullPointerException | IllegalArgumentException e) {
			Log.e(TAG, "onCreate: No media player available.", e);
			// TODO: consider sending a notification to notification center
			// TODO: during testing, leave following line commented out
			//            finish(); // don't waste battery; it's likely user won't see alarm
		}
	}
	
	
	@SuppressWarnings({"WeakerAccess", "unused"})
	public void onClickDismiss(View view) {
		if (mAlarm.isMathQuestionsEnabled()) {
			Bundle bundle = new Bundle();
			bundle.putParcelable("alarm", mAlarm);
			
			MathQuestionDialogFragment mathDialog = new MathQuestionDialogFragment();
			mathDialog.setArguments(bundle);
			mathDialog.show(getSupportFragmentManager(), "Dismiss");
		} else {
			finish();
		}
	}
	
	
	public void onClickSnooze(View view) {
		mAlarm.setSnoozeCount((byte) (mAlarm.getSnoozeCount() + 1));
		manager.update(mAlarm);
		
		snoozeAlarm();
		finish();
	}
	
	
	@Override
	public void onDestroy() {
		try {
			player.release();
		} catch (NullPointerException | IllegalStateException ignored) {
			// Ignored; we're destroying activity anyway
		} finally {
			super.onDestroy();
		}
		
	}
	
	
	@Override
	public void onDialogButtonClick(DialogInterface dialog, int which) {
		if (which == Dialog.BUTTON_POSITIVE) {
			mAlarm.setSnoozeCount((byte) 0);
			if (mAlarm.isRepeatAlarm()) {
				setRepeatAlarm();
			}
			
			if (player != null && player.isPlaying()) {
				player.stop();
			}
			
			finish();
		} else {
			Toast.makeText(this, "Wrong answer; try again!", Toast.LENGTH_LONG).show();
			onClickDismiss(new View(this));
		}
		
	}
	
	
	private void populateViews() {
		AlarmFormatter alarmFormatter = new AlarmFormatter(mAlarm);
		
		TextView time = findViewById(R.id.alarmWakeTime);
		time.setText(alarmFormatter.formatTime());
		
		TextView label = findViewById(R.id.alarmWakeLabel);
		label.setText(mAlarm.getLabel());
		
		Button snooze = findViewById(R.id.snoozeButton);
		if (!mAlarm.isSnoozeEnabled()) {
			snooze.setVisibility(View.GONE);
			snooze.setClickable(false);
		} else {
			snooze.setVisibility(View.VISIBLE);
			
			if (mAlarm.getSnoozeCount() >= 3) {
				snooze.setAlpha(0.5f);
				snooze.setClickable(false);
			} else {
				snooze.setClickable(true);
				snooze.setAlpha(1f);
			}
		}
		
		
	}
	
	
	@SuppressWarnings("EmptyMethod")
	private void setRepeatAlarm() {
		// TODO: create new PendingIntent relevant to this repeating alarm
	}
	
	
	private void snoozeAlarm() {
		Intent alarmWakeIntent;
		try {
			alarmWakeIntent = new Intent(createPackageContext(getPackageName(), CONTEXT_INCLUDE_CODE),
			                             AlarmReceiver.class);
		} catch (PackageManager.NameNotFoundException nnfe) {
			alarmWakeIntent = new Intent(this, AlarmReceiver.class);
			Log.w("AlarmEditActivity", "setAlarmInOS", nnfe);
		}
		
		Bundle bundle = new Bundle();
		bundle.putParcelable("alarm", mAlarm);
		alarmWakeIntent.putExtras(bundle);
		
		PendingIntent futureAlarm
				= PendingIntent.getBroadcast(getApplicationContext(), mAlarm.hashCode(), alarmWakeIntent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		ZonedDateTime now = LocalDateTime.now().atZone(TimeZone.getDefault().toZoneId());
		long timeSinceEpoch = TimeUnit.SECONDS.toMillis(now.toEpochSecond());
		timeSinceEpoch += TimeUnit.MINUTES.toMillis(3L);
		
		try {
			assert alarmManager != null;
			alarmManager.set(AlarmManager.RTC_WAKEUP, timeSinceEpoch, futureAlarm);
		} catch (AssertionError ae) {
			Log.e("AlarmWakeActivity", "snoozeAlarm: ALARM_SERVICE unavailable.");
			this.onDestroy();
		}
	}
}


