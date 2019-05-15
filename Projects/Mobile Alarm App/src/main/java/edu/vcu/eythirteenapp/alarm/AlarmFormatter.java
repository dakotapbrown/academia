package edu.vcu.eythirteenapp.alarm;

import java.time.format.DateTimeFormatter;

public class AlarmFormatter {
	
	private final Alarm mAlarm;
	
	
	public AlarmFormatter(Alarm alarm) {
		mAlarm = alarm;
	}
	
	
	public String formatRepeatDaysForDisplay() {
		StringBuilder result = new StringBuilder();
		String[] repeatDays = mAlarm.getRepeatDays();
		
		// return early, if possible
		if (repeatDays[0] == null) {
			return "Never";
		}
		
		for (int i = 0; i < repeatDays.length; i++) {
			if (repeatDays[i] != null && !repeatDays[i].equals("")) {
				result.append(repeatDays[i], 0, 3).append(" ");
				i++;
			}
		}
		
		return result.toString();
	}
	
	
	public String formatRepeatDaysForStorage() {
		StringBuilder result = new StringBuilder();
		String[] repeatDays = mAlarm.getRepeatDays();
		
		if (repeatDays[0] == null) {
			return "";
		}
		
		for (int i = 0; i < repeatDays.length; i++) {
			if (repeatDays[i] != null) {
				result.append(repeatDays[i]).append(" ");
				i++;
			}
		}
		
		return result.toString();
	}
	
	
	public String formatTime() {
		return mAlarm.getTime().format(DateTimeFormatter.ofPattern("h:mm a"));
	}
	
	
	public int getHourOfTime() {
		return mAlarm.getTime().getHour();
	}
	
	
	public int getMinuteOfTime() {
		return mAlarm.getTime().getMinute();
	}
	
}
