package edu.vcu.eythirteenapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class AlarmDatabaseHelper extends SQLiteOpenHelper {
    
    // table name
    public static final String TABLE_NAME = "Alarms";
    
    // columns
    public static final String _ID = "_id";
    public static final String TIME = "Time";
    public static final String LABEL = "Label";
    public static final String REPEAT_DAYS = "RepeatDays";
    private static final String REPEAT_ALARM = "RepeatAlarm";
    public static final String DIFFICULTY = "Difficulty";
    public static final String SNOOZE_COUNT = "SnoozeCount";
    public static final String VOLUME = "Volume";
    public static final String ATTRIBUTES = "Attributes";
    private static final String[] VERSION_1_COL_NAMES
            = {_ID, TIME, LABEL, REPEAT_DAYS, REPEAT_ALARM, DIFFICULTY, SNOOZE_COUNT, VOLUME};
// --Commented out by Inspection START (12/6/2018 12:22 AM):
//    public static final String[] VERSION_2_COL_NAMES
//            = {_ID, TIME, LABEL, REPEAT_DAYS, ATTRIBUTES, DIFFICULTY, SNOOZE_COUNT, VOLUME};
// --Commented out by Inspection STOP (12/6/2018 12:22 AM)

    // database name
    public static final String DB_NAME = "Alarms.db";
    
    // current database version
    private static final int VERSION = 2;
    
    // query to create empty table
    private static final String CREATION_QUERY
            = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TIME," +
                                    " %s TEXT NOT NULL, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, " +
                                    "%s INTEGER );",
                            TABLE_NAME,
                            _ID,
                            TIME,
                            LABEL,
                            REPEAT_DAYS,
                            ATTRIBUTES,
                            DIFFICULTY,
                            SNOOZE_COUNT,
                            VOLUME
                           );
    
    
    public AlarmDatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATION_QUERY);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            final String csvColNames_v1 = String.join(", ", VERSION_1_COL_NAMES);
            
            db.beginTransaction();
            try {
                db.execSQL(String.format("CREATE TEMPORARY TABLE temp(%s);", csvColNames_v1));
                db.execSQL(String.format("INSERT INTO temp SELECT %s FROM %s;", csvColNames_v1, TABLE_NAME));
                db.execSQL(String.format("DROP TABLE %s;", TABLE_NAME));
                db.execSQL(CREATION_QUERY);
                db.execSQL(String.format("INSERT INTO %s SELECT %s FROM temp;",
                                         TABLE_NAME,
                                         csvColNames_v1));
                //noinspection SyntaxError
                db.execSQL("DROP TABLE temp;");
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }
    
}
