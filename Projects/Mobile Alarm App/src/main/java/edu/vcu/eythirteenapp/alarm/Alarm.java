package edu.vcu.eythirteenapp.alarm;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.TimeZone;

/**
 * This class is a convenience class that serves to programmatically represent an Alarm.
 *
 * @author Dakota Brown
 */
@SuppressWarnings("ALL")
public class Alarm implements Parcelable {
	
	// For object to be Parcelable
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Alarm createFromParcel(Parcel in) {
			return new Alarm(in);
		}
		
		
		public Alarm[] newArray(int size) {
			return new Alarm[size];
		}
	};
	
	public static final byte REPEAT = 0b1;
	public static final byte SNOOZE = 0b10;
	public static final byte EXPRESSION = 0b100;
	private static final byte AUTO_SHUTOFF = 0b1000;
	
	private int m_ID;
	private String mLabel;
	private LocalTime mTime;
	private String[] mRepeatDays = new String[] { "" };
	private boolean mRepeatAlarm;
	private boolean mSnoozeEnabled;
	private boolean mExpressionEnabled;
	private boolean mAutoShutOff;
	private byte mDifficulty;
	private byte mSnoozeCount;
	private byte mVolume;
	private byte mAttributes;
	private Uri mTone;
	private int mPasscode;
	
	
	public Alarm() {
		mTime = LocalTime.now().withSecond(0).withNano(0);
		mLabel = "Alarm";
		//        mRepeatDays = new String[7];
		setRepeatAlarm(false);
		setSnoozeEnabled(true);
		setExpressionEnabled(true);
	}
	
	
	@SuppressWarnings("unused")
	public Alarm(int _id, LocalTime time, String label, String[] repeatDays) {
		m_ID = _id;
		mTime = time;
		mLabel = label;
		//        mRepeatDays = repeatDays.length < 7 ? Arrays.copyOf(repeatDays, 7) : repeatDays;
	}
	
	
	private Alarm(Parcel in) {
		m_ID = in.readInt();
		mTime = (LocalTime) in.readSerializable();
		mLabel = in.readString();
		in.readStringArray(mRepeatDays);
		setAttributes(in.readByte());
		mDifficulty = in.readByte();
		mSnoozeCount = in.readByte();
		mVolume = in.readByte();
		mTone = in.readParcelable(Uri.class.getClassLoader());
		mPasscode = in.readInt();
	}
	
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	
	@Override
	public boolean equals(Object other) {
		if (this == other) { return true; }
		
		if (!(other instanceof Alarm)) { return false; }
		
		Alarm otherAlarm = (Alarm) other;
		
		return mTime.equals(otherAlarm.mTime) &&
				       Arrays.equals(mRepeatDays, otherAlarm.mRepeatDays)
				       && mLabel.equals(otherAlarm.mLabel)
				       && mRepeatAlarm == otherAlarm.mRepeatAlarm;
	}
	
	
	public boolean filterEquals(Object other) {
		if (this == other) { return true; }
		
		if (!(other instanceof Alarm)) { return false; }
		
		Alarm otherAlarm = (Alarm) other;
		
		// Alarms are equal if they represent the same time and day configuration.
		return mTime.equals(otherAlarm.mTime) &&
				       repeatDaysEqual(mRepeatDays, otherAlarm.mRepeatDays);
	}
	
	
	public byte getAttributes() {
		return mAttributes;
	}
	
	
	public int getDifficulty() {
		return mDifficulty;
	}
	
	
	public long getEpochTimeInMillis() {
		return getDateTime().atZone(TimeZone.getDefault().toZoneId()).toEpochSecond() * 1000;
	}
	
	
	public int getID() {
		return m_ID;
	}
	
	
	public String getLabel() {
		return mLabel;
	}
	
	
	public String[] getRepeatDays() {
		return mRepeatDays;
	}
	
	
	public int getSnoozeCount() {
		return mSnoozeCount;
	}
	
	
	public LocalTime getTime() {
		return mTime;
	}
	
	
	public Uri getTone() {
		return mTone;
	}
	
	
	public int getVolume() { return mVolume; }
	
	
	@Override
	public int hashCode() {
		return (Integer.hashCode(m_ID)
				        + mTime.hashCode()
				        + mLabel.hashCode()
				        + Boolean.hashCode(mRepeatAlarm)
				        + Arrays.hashCode(mRepeatDays));
	}
	
	
	public boolean isAutoShutOff() {
		return mAutoShutOff;
	}
	
	
	public boolean isMathQuestionsEnabled() {
		return mExpressionEnabled;
	}
	
	
	public boolean isRepeatAlarm() {
		return mRepeatAlarm;
	}
	
	
	public boolean isSnoozeEnabled() {
		return mSnoozeEnabled;
	}
	
	
	public Alarm setAttributes(byte attributes) {
		mAttributes = attributes;
		mRepeatAlarm = (attributes % 2) != 0;
		attributes >>= 1;
		mSnoozeEnabled = (attributes % 2) != 0;
		attributes >>= 1;
		mExpressionEnabled = (attributes % 2) != 0;
		attributes >>= 1;
		mAutoShutOff = (attributes % 2) != 0;
		
		return this;
	}
	
	
	public Alarm setAutoShutOff(boolean autoShutOff) {
		mAutoShutOff = autoShutOff;
		if (autoShutOff) {
			mAttributes |= AUTO_SHUTOFF;
		} else {
			mAttributes &= ~AUTO_SHUTOFF;
		}
		
		return this;
	}
	
	
	public Alarm setDifficulty(byte difficulty) {
		mDifficulty = difficulty;
		
		return this;
	}
	
	
	public Alarm setExpressionEnabled(boolean expressionEnabled) {
		mExpressionEnabled = expressionEnabled;
		if (expressionEnabled) {
			mAttributes |= EXPRESSION;
		} else {
			mAttributes &= ~EXPRESSION;
		}
		
		return this;
	}
	
	
	public Alarm setLabel(String label) {
		mLabel = label;

		return this;
	}
	
	
	public Alarm setPasscode(final int passcode) {
		mPasscode = passcode;

		return this;
	}
	
	
	public Alarm setRepeatAlarm(boolean isRepeatAlarm) {
		mRepeatAlarm = isRepeatAlarm;
		if (isRepeatAlarm) {
			mAttributes |= REPEAT;
		} else {
			mAttributes &= ~REPEAT;
		}

		return this;
	}
	
	
	@SuppressWarnings("unused")
	public Alarm setRepeatDays(String[] repeatDays) {
		mRepeatDays = repeatDays;
		
		return this;
	}
	
	
	public Alarm setSnoozeCount(byte snoozeCount) {
		mSnoozeCount = snoozeCount;

		return this;
	}
	
	
	public Alarm setSnoozeEnabled(boolean snoozeEnabled) {
		mSnoozeEnabled = snoozeEnabled;
		if (snoozeEnabled) {
			mAttributes |= SNOOZE;
		} else {
			mAttributes &= ~SNOOZE;
		}

		return this;
	}
	
	
	public Alarm setTime(LocalTime time) {
		mTime = time;
		
		return this;
	}
	
	
	public Alarm setTone(Uri tone) {
		mTone = tone;
		
		return this;
	}
	
	
	public Alarm setVolume(byte volume) {
		mVolume = volume;
		
		return this;
	}
	
	
	@Override
	public String toString() {
		return String.format(
				"Alarm:\n\tID: %d\n\tTime: %s\n\tLabel: %s\n\tRepeating alarm: %s\n\tDays alarm repeats:" +
						" %s\n",
				m_ID,
				mTime.format(DateTimeFormatter.ofPattern("h:mm a")),
				mLabel,
				mRepeatAlarm ? "Yes" : "No",
				mRepeatDays.length != 0 ? Arrays.toString(mRepeatDays) : "None");
	}
	
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(m_ID);
		dest.writeSerializable(mTime);
		dest.writeString(mLabel);
		dest.writeStringArray(mRepeatDays);
		dest.writeByte(mAttributes);
		dest.writeByte(mDifficulty);
		dest.writeByte(mSnoozeCount);
		dest.writeByte(mVolume);
		dest.writeParcelable(mTone, 0);
		dest.writeInt(mPasscode);
	}
	
	
	private LocalDateTime getDateTime() {
		return mTime.atDate(LocalDate.now());
	}
	
	
	private boolean repeatDaysEqual(String[] repeatDays, String[] other) {
		
		boolean result;
		for (int i = 0; i < repeatDays.length; i++) {
			boolean currentNotNull = repeatDays[i] != null;
			boolean otherNotNull = other[i] != null;
			if (currentNotNull && otherNotNull) {
				result = repeatDays[i].equals(other[i]);
				if (!result) {
					return false;
				}
			} else {
				return false;
			}
		}
		
		return true;
	}
}
