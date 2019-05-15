package edu.vcu.eythirteenapp.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import edu.vcu.eythirteenapp.alarm.Alarm;
import edu.vcu.eythirteenapp.alarm.AlarmFormatter;

public class AlarmDatabaseManager {
	
	private final Context mContext;
	private AlarmDatabaseHelper mAlarmDatabaseHelper;
	private SQLiteDatabase m_sqlDatabase;
	
	
	public AlarmDatabaseManager(Context context) {
		mContext = context;
		this.open();
	}
	
	
	public void close() {
		mAlarmDatabaseHelper.close();
	}
	
	
	public void delete(int _id) {
		m_sqlDatabase.delete(AlarmDatabaseHelper.TABLE_NAME, AlarmDatabaseHelper._ID + " = " + _id, null);
	}
	
	
	public Cursor fetch() {
		
		String selectQuery = String.format("SELECT * FROM %s ORDER BY %s",
		                                   AlarmDatabaseHelper.TABLE_NAME,
		                                   AlarmDatabaseHelper.TIME);
		
		Cursor cursor = m_sqlDatabase.rawQuery(selectQuery, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		
		return cursor;
	}
	
	
	public Alarm fetchById(int _id) {
		Cursor cursor;
		Alarm alarm = null;
		
		String sql = String.format("SELECT * FROM %s WHERE %s = %d",
		                           AlarmDatabaseHelper.TABLE_NAME,
		                           AlarmDatabaseHelper._ID,
		                           _id);
		
		cursor = m_sqlDatabase.rawQuery(sql, null);
		if (cursor != null) {
			cursor.moveToFirst();
			String timeAsStr
					= cursor.getString(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.TIME));
			String label
					= cursor.getString(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.LABEL));
			String repeatDaysAsStr
					= cursor.getString(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.REPEAT_DAYS));
			byte attributes
					= (byte) cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.ATTRIBUTES));
			int difficulty
					= cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.DIFFICULTY));
			int snoozeCount
					= cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.SNOOZE_COUNT));
			int volume
					= cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.VOLUME));
			
			LocalTime time = LocalTime.parse(timeAsStr, DateTimeFormatter.ofPattern("HH:mm"));
			String[] repeatDays = repeatDaysAsStr.split("\\s", 7);
			if (repeatDays.length == 0) {
				repeatDays = new String[7];
			}
			
			alarm = new Alarm(_id, time, label, repeatDays);
			alarm.setAttributes(attributes);
			alarm.setSnoozeCount((byte) snoozeCount);
			alarm.setDifficulty((byte) difficulty);
			alarm.setVolume((byte) volume);
			
			cursor.close();
		}
		
		return alarm;
	}
	
	
	public void insert(Alarm alarm) {
		AlarmFormatter formatter = new AlarmFormatter(alarm);
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(AlarmDatabaseHelper.TIME, alarm.getTime().toString());
		contentValues.put(AlarmDatabaseHelper.LABEL, alarm.getLabel());
		contentValues.put(AlarmDatabaseHelper.REPEAT_DAYS, formatter.formatRepeatDaysForStorage());
		contentValues.put(AlarmDatabaseHelper.ATTRIBUTES, (int) alarm.getAttributes());
		contentValues.put(AlarmDatabaseHelper.DIFFICULTY, alarm.getDifficulty());
		contentValues.put(AlarmDatabaseHelper.SNOOZE_COUNT, alarm.getSnoozeCount());
		contentValues.put(AlarmDatabaseHelper.VOLUME, alarm.getVolume());
		
		m_sqlDatabase.insert(AlarmDatabaseHelper.TABLE_NAME, null, contentValues);
	}
	
	
	public void update(Alarm alarm) {
		AlarmFormatter formatter = new AlarmFormatter(alarm);
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(AlarmDatabaseHelper._ID, alarm.getID());
		contentValues.put(AlarmDatabaseHelper.TIME, alarm.getTime().toString());
		contentValues.put(AlarmDatabaseHelper.LABEL, alarm.getLabel());
		contentValues.put(AlarmDatabaseHelper.REPEAT_DAYS, formatter.formatRepeatDaysForStorage());
		contentValues.put(AlarmDatabaseHelper.ATTRIBUTES, (int) alarm.getAttributes());
		contentValues.put(AlarmDatabaseHelper.DIFFICULTY, alarm.getDifficulty());
		contentValues.put(AlarmDatabaseHelper.SNOOZE_COUNT, alarm.getSnoozeCount());
		contentValues.put(AlarmDatabaseHelper.VOLUME, alarm.getVolume());
		
		m_sqlDatabase.update(AlarmDatabaseHelper.TABLE_NAME,
		                     contentValues,
		                     AlarmDatabaseHelper._ID + " = " + alarm.getID(),
		                     null);
	}
	
	
	private void open() throws SQLException {
		mAlarmDatabaseHelper = new AlarmDatabaseHelper(mContext);
		m_sqlDatabase = mAlarmDatabaseHelper.getWritableDatabase();
	}
	
}

