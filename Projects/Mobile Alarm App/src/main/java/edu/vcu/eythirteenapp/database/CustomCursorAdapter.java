package edu.vcu.eythirteenapp.database;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import edu.vcu.eythirteenapp.R;

/**
 * The {@code CustomCursorAdapter} class is a bridge between the SQLite Database that describes the attributes
 * of an {@code Alarm} and the {@code ListView} object responsible for displaying those attributes. It is
 * within the {@code bindView} method call that the column data (for any row) is bounded to the appropriate
 * receiving {@code View}.
 *
 * @author Dakota Brown
 */
public class CustomCursorAdapter extends CursorAdapter {
	
	public CustomCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor, 0);
	}
	
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		/* Find fields to populate in inflated template */
		TextView alarm_ID = view.findViewById(R.id.alarmID);
		TextView alarmTime = view.findViewById(R.id.alarmTime);
		TextView alarmLabel = view.findViewById(R.id.alarmLabel);
		TextView alarmRepeatDays = view.findViewById(R.id.alarmRepeatDays);
		
		/* Extract properties from cursor */
		final int _idColIndx = cursor.getColumnIndexOrThrow(AlarmDatabaseHelper._ID);
		final int timeColIndx = cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.TIME);
		final int labelColIndx = cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.LABEL);
		final int repeatDaysColIndx = cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.REPEAT_DAYS);
		
		int _id = cursor.getInt(_idColIndx);
		String timeAsString = cursor.getString(timeColIndx);
		String label = cursor.getString(labelColIndx);
		String repeatDays = cursor.getString(repeatDaysColIndx);
		
		/* Populate fields with extracted properties */
		LocalTime time = LocalTime.parse(timeAsString, DateTimeFormatter.ofPattern("HH:mm"));
		String formattedTime = time.format(DateTimeFormatter.ofPattern("h:mm a"));
		
		alarm_ID.setText(String.valueOf(_id));
		alarmTime.setText(formattedTime);
		alarmLabel.setText(label);
		alarmRepeatDays.setText(repeatDays);
	}
	
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.fragment_list, parent, false);
	}
}
