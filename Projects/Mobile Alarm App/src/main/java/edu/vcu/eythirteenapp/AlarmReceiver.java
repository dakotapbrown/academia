package edu.vcu.eythirteenapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent alarmWakeIntent = new Intent(context, AlarmWakeActivity.class);
		
		try {
			assert intent != null;
			alarmWakeIntent.replaceExtras(intent);
			context.startActivity(alarmWakeIntent);
		} catch (AssertionError ae) {
			Log.e("AlarmReceiver", "onReceive: No alarm info found.");
		}
		
	}
	
}
