package edu.vcu.eythirteenapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.vcu.eythirteenapp.alarm.Alarm;
import edu.vcu.eythirteenapp.alarm.AlarmFormatter;
import edu.vcu.eythirteenapp.database.AlarmDatabaseHelper;
import edu.vcu.eythirteenapp.database.AlarmDatabaseManager;
import edu.vcu.eythirteenapp.dialogs.DialogListener;
import edu.vcu.eythirteenapp.dialogs.LabelEditDialogFragment;
import edu.vcu.eythirteenapp.dialogs.SetPasscodeDialogFragment;
import edu.vcu.eythirteenapp.dialogs.TimePickerDialogFragment;

import static edu.vcu.eythirteenapp.dialogs.LabelEditDialogFragment.LABEL_EDIT_VIEW_ID;
import static edu.vcu.eythirteenapp.dialogs.SetPasscodeDialogFragment.PASSCODE_EDIT_VIEW_ID;

public class AlarmEditActivity extends FragmentActivity
		implements TimePickerDialogFragment.TimePickerDialogListener, DialogListener {
	
	private Alarm mAlarm;
	private boolean isUpdated;
	private AlarmDatabaseManager manager;
	private List<CheckBox> checkBoxObserver;
	private SeekBar volumeSeekBar;
	
	
	@SuppressLint("FindViewByIdCast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_edit);
		
		checkBoxObserver = new ArrayList<>();
		checkBoxObserver.add(findViewById(R.id.easyCheckBox));
		checkBoxObserver.add(findViewById(R.id.mediumCheckBox));
		checkBoxObserver.add(findViewById(R.id.hardCheckBox));
		
		manager = new AlarmDatabaseManager(this);
		
		// get Alarm ID from Intent (if one exists)
		Intent receivedIntent = getIntent();
		Bundle intentBundle = receivedIntent.getBundleExtra("alarm");
		
		if (intentBundle != null) {
			setAlarm(intentBundle.getParcelable("alarm"));
		} else {
			// new alarm
			setAlarm(new Alarm());
		}
		
		
		populateViews();
		initControls();
		
	}
	
	
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
		if (resultCode == Activity.RESULT_OK && requestCode == R.id.toneEditButton >> 16) {
			Uri tone = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			Ringtone ringtone = RingtoneManager.getRingtone(this, tone);
			
			if (tone != null && !tone.equals(mAlarm.getTone())) {
				isUpdated = true;
				mAlarm.setTone(tone);
				
				TextView alarmTone = findViewById(R.id.alarmTone);
				alarmTone.setText(ringtone.getTitle(this));
			}
		}
	}
	
	
	public void onClickCancel(View view) {
		finish();
	}
	
	
	public void onClickCheckBox(View view) {
		isUpdated = true;
		
		CheckBox clickedCheckBox = (CheckBox) view;
		for (CheckBox checkBox : checkBoxObserver) {
			if (!checkBox.equals(clickedCheckBox)) {
				if (checkBox.isChecked()) {
					checkBox.toggle();
				}
			}
		}
		
		final int viewID = view.getId();
		switch (viewID) {
			case R.id.easyCheckBox:
				mAlarm.setDifficulty((byte) 0);
				break;
			case R.id.mediumCheckBox:
				mAlarm.setDifficulty((byte) 1);
				break;
			case R.id.hardCheckBox:
				mAlarm.setDifficulty((byte) 2);
				break;
		}
	}
	
	
	public void onClickDelete(View view) {
		if (mAlarm.getID() != 0) {
			manager.delete(mAlarm.getID());
		} else {
			return;
		}
		
		finish();
	}
	
	
	public void onClickLabelEdit(View view) {
		LabelEditDialogFragment labelEdit = new LabelEditDialogFragment();
		labelEdit.show(getSupportFragmentManager(), "Label");
	}
	
	
	public void onClickRepeatSwitch(View view) {
		// TODO: make user choose which days to repeat
		isUpdated = true;
		
		Switch sw = (Switch) view;
		mAlarm.setRepeatAlarm(sw.isChecked());
	}
	
	
	public void onClickExpressionSwitch(View view) {
		Switch sw = (Switch) view;
		
		if (!sw.isChecked()) {
			(new AlertDialog.Builder(this))
					.setTitle(R.string.expressionDisabledDialog)
					.setMessage(R.string.expressionDisabledMsg)
					.setPositiveButton("Yes", (dialog, which) -> {
						SetPasscodeDialogFragment passcodeDialog = new SetPasscodeDialogFragment();
						passcodeDialog.show(getSupportFragmentManager(), "Passcode");
						dialog.dismiss();
					})
					.setNegativeButton("No", (dialog, which) -> {
						sw.toggle();
						dialog.dismiss();
					})
					.setCancelable(false)
					.show();
		} else {
			isUpdated = true;
			mAlarm.setExpressionEnabled(true);
		}
	}
	
	public void onClickSave(View view) {
		if (isUpdated || mAlarm.getID() == 0) {
			if (isAlarmUnique()) {
				if (mAlarm.getID() != 0) {
					manager.update(mAlarm);
				} else {
					manager.insert(mAlarm);
				}
				setAlarmInOS();
				
				finish();
			} else {
				(new AlertDialog.Builder(this))
						.setTitle(R.string.existingAlarmDialog)
						.setMessage(R.string.existingAlarmMessage)
						.setPositiveButton(R.string.okayButton, (dialog, which) -> dialog.dismiss())
						.setCancelable(false)
						.show();
			}
		} else {
			finish();
		}
	}
	
	
	public void onClickTimeEdit(View view) {
		Bundle alarmInfo = new Bundle();
		alarmInfo.putParcelable("alarmInfo", mAlarm);
		
		DialogFragment timePickerFragment = new TimePickerDialogFragment();
		timePickerFragment.setArguments(alarmInfo);
		timePickerFragment.show(getSupportFragmentManager(), "timePicker");
	}
	
	
	public void onClickToneEdit(View view) {
		Intent tonePicker = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		tonePicker.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
		tonePicker.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Alarm Tone");
		tonePicker.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
		startActivityForResult(tonePicker, R.id.toneEditButton >> 16);
	}
	
	
	@Override
	protected void onDestroy() {
		try {
			manager.close();
		} finally {
			super.onDestroy();
		}
	}
	
	
	public void onClickSnoozeSwitch(View view) {
		isUpdated = true;
		mAlarm.setSnoozeEnabled(((Switch) view).isChecked());
	}
	
	
	@Override
	public void onTimePickerDialogClose(Alarm alarm) {
		isUpdated = true;
		mAlarm = alarm;
		AlarmFormatter alarmFormatter = new AlarmFormatter(mAlarm);
		String time = alarmFormatter.formatTime();
		TextView alarmTime = findViewById(R.id.editAlarmTime);
		alarmTime.setText(time);
	}
	

	@Override
	public void onDialogButtonClick(DialogInterface dialog, int which) {
		Dialog dialogView = (Dialog) dialog;
		EditText editText = dialogView.findViewById(LABEL_EDIT_VIEW_ID);
		
		if (editText != null) {
			String label = editText.getText().toString();
			if (!label.equals(mAlarm.getLabel())) {
				mAlarm.setLabel(label);
				isUpdated = true;
			} else if (label.equals("")) {
				mAlarm.setLabel("Alarm");
				isUpdated = true;
			}
			
			TextView labelView = findViewById(R.id.editAlarmLabel);
			labelView.setText(mAlarm.getLabel());
		} else {
			editText = dialogView.findViewById(PASSCODE_EDIT_VIEW_ID);
			final String s = editText.getText().toString();
			if (which == Dialog.BUTTON_POSITIVE) {
				if (!s.equals("")) {
					mAlarm.setExpressionEnabled(false);
					mAlarm.setPasscode(Integer.parseInt(s));
					isUpdated = true;
				}
			} else {
				Switch sw = findViewById(R.id.expressionEnabledSwitch);
				sw.toggle();
			}
		}
		
	}
	
	private void initControls() {
		volumeSeekBar = findViewById(R.id.VolumeControl);
		
		volumeSeekBar.setOnSeekBarChangeListener(
				new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
						mAlarm.setVolume((byte) volumeSeekBar.getProgress());
					}
					
					
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) { /* not implemented */ }
					
					
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) { /* not implemented */ }
				});
	}
	
	
	private List<Alarm> interrogateAlarmDatabase() {
		Cursor cursor = manager.fetch();
		List<Alarm> alarms = new ArrayList<>();
		
		if (cursor != null) {
			cursor.moveToFirst();
			
			for (int i = 1; i <= cursor.getCount(); i++) {
				final int _idColIndex = cursor.getColumnIndexOrThrow(AlarmDatabaseHelper._ID);
				final int timeColIndex = cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.TIME);
				final int repeatDaysColIndex = cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.REPEAT_DAYS);
				
				int _id = cursor.getInt(_idColIndex);
				String timeAsString = cursor.getString(timeColIndex);
				String repeatDaysAsStr = cursor.getString(repeatDaysColIndex);
				
				String[] repeatDays = repeatDaysAsStr.split("\\s", 7);
				LocalTime time = LocalTime.parse(timeAsString, DateTimeFormatter.ofPattern("HH:mm"));
				
				alarms.add(new Alarm(_id, time, null, repeatDays));
				
				if (i != cursor.getCount()) {
					cursor.moveToNext();
				}
			}
			
			cursor.close();
		}
		
		return alarms;
	}
	
	
	private boolean isAlarmUnique() {
		List<Alarm> alarms = interrogateAlarmDatabase();
		if (alarms.size() == 0) { return true; }
		
		for (Alarm alarm : alarms) {
			if (mAlarm.getID() != alarm.getID()
					    && mAlarm.filterEquals(alarm)) {
				return false;
			}
		}
		
		return true;
	}
	
	
	private void populateViews() {
		AlarmFormatter alarmFormatter = new AlarmFormatter(mAlarm);
		
		TextView tv = findViewById(R.id.editAlarmTime);
		tv.setText(alarmFormatter.formatTime());
		
		tv = findViewById(R.id.editAlarmLabel);
		tv.setText(mAlarm.getLabel());
		
		tv = findViewById(R.id.editAlarmRepeatDays);
		tv.setText(alarmFormatter.formatRepeatDaysForDisplay());
		
		Switch sw = findViewById(R.id.repeatEnabledSwitch);
		if (mAlarm.isRepeatAlarm()) {
			sw.toggle();
		}
		
		sw = findViewById(R.id.snoozeEnabledSwitch);
		if (mAlarm.isSnoozeEnabled()) {
			sw.toggle();
		}
		
		sw = findViewById(R.id.expressionEnabledSwitch);
		if (mAlarm.isMathQuestionsEnabled()) {
			sw.toggle();
		}
		
		SeekBar sb = findViewById(R.id.VolumeControl);
		sb.setProgress(mAlarm.getVolume());
		
		CheckBox checkBox = checkBoxObserver.get(mAlarm.getDifficulty());
		checkBox.toggle();
		
		if (mAlarm.getTone() == null) {
			mAlarm.setTone(RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM));
		}
		tv = findViewById(R.id.alarmTone);
		tv.setText(RingtoneManager.getRingtone(this, mAlarm.getTone()).getTitle(this));
	}
	
	
	private void setAlarm(Alarm alarm) {
		mAlarm = alarm;
	}
	
	
	private void setAlarmInOS() {
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
		alarmWakeIntent.putExtra("alarm", bundle);
		//        alarmWakeIntent.putExtras(bundle);
		PendingIntent futureAlarm
				= PendingIntent.getBroadcast(getApplicationContext(), mAlarm.hashCode(), alarmWakeIntent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		long timeSinceEpoch = mAlarm.getEpochTimeInMillis();
		LocalTime now = LocalTime.now().withSecond(0).withNano(0);
		if (mAlarm.getTime().isBefore(now)) {
			timeSinceEpoch += TimeUnit.HOURS.toMillis(24);
		}
		
		try {
			assert alarmManager != null;
			alarmManager.set(AlarmManager.RTC_WAKEUP, timeSinceEpoch, futureAlarm);
		} catch (AssertionError ae) {
			Log.e("AlarmEditActivity", "setAlarmInOS: ALARM_SERVICE unavailable.");
			this.onDestroy();
		}
	}
}
