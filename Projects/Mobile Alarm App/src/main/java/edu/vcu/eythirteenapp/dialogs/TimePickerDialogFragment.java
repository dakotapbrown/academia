package edu.vcu.eythirteenapp.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.time.LocalTime;

import edu.vcu.eythirteenapp.alarm.Alarm;
import edu.vcu.eythirteenapp.alarm.AlarmFormatter;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
	
	private Alarm mAlarm;
	
	
	/*
	 * Code from: Android Developer API
	 * Slightly edited to be more up to date with current API level
	 */
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use Alarm object data to populate values for picker
		Bundle alarmInfo = getArguments();
		if (alarmInfo != null) {
			mAlarm = alarmInfo.getParcelable("alarmInfo");
		}
		
		AlarmFormatter alarmFormatter = new AlarmFormatter(mAlarm);
		int hour = mAlarm != null ? alarmFormatter.getHourOfTime() : LocalTime.now().getHour();
		int minute = mAlarm != null ? alarmFormatter.getMinuteOfTime() : LocalTime.now().getMinute();
		
		if (hour > 12) {
			hour = hour - 12;
		}
		
		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute, false);
	}
	
	
	@Override
	public void onTimeSet(TimePicker view, int hour, int minute) {
		// Send time values back to caller
		mAlarm.setTime(LocalTime.of(hour, minute));
		TimePickerDialogListener activity = (TimePickerDialogListener) getActivity();
		if (activity != null) {
			activity.onTimePickerDialogClose(mAlarm);
		}
		
		this.dismiss();
	}
	
	
	public interface TimePickerDialogListener {
		void onTimePickerDialogClose(Alarm alarm);
	}
	
}