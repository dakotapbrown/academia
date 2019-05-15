/**
 * A class whose instances serve as an object representation of a valid, 
 * physical calendar date (based on the Gregorian calendar).
 * 
 * @author Dakota Brown
 * @version CMSC 256, Section 001
 * @since February 3, 2019
 */
public class CustomDate {

	// instance variables
	private int month;
	private int day;
	private int year;
	
	/**
	 * Constructs a default {@code CustomDate} that's set to 
	 * January 1, 2000 (1/1/2000). 
	 */
	public CustomDate() {
		this(1, 1, 2000);
	}
	
	/**
	 * Constructs a {@code CustomDate} object that represents a date whose
	 * value is set to the specified {@code month}, {@code day}, and 
	 * {@code year}. 
	 * 
	 * <p>The {@code CustomDate} object can represent any date beyond the 
	 * first year (e.g. {@code year} must be greater than 0).</p>
	 * 
	 * @param month Numerical value corresponding to the month to be set 
	 * @param day Number of day to set 
	 * @param year Number of year to set
	 * 
	 * @throws IllegalArgumentException 
	 * 			If the specified date is not valid date
	 */
	public CustomDate(int month, int day, int year) {
		setMonth(month);
		setDay(day);
		setYear(year);
		this.initialized();
	}

	/**
	 * Returns the numerical representation of the month. 
	 * 
	 * <p>The numbers 1-12 represent the months January-December, 
	 * respectively.</p>
	 * 
	 * @return The month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * Sets the month to a number between 1 and 12.
	 * 
	 * <p>The numbers 1-12 represent the months January-December, 
	 * respectively.</p>
	 * 
	 * @param month The month to set.
	 */
	public void setMonth(int month) throws IllegalArgumentException {
		if (month <= 12 && month >= 1) {
			this.month = month;
		} else {
			throw new IllegalArgumentException("Month must be "
												+ "between 1 and 12.");
		}
	}
	
	/**
	 * Returns the value of the day.
	 * 
	 * @return The day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * Sets the value of the day to value between 1 and 31.
	 * 
	 * @param day The day to set
	 * 
	 * @throws IllegalArgumentException 
	 * 			If {@code day} is not between 1 and 31 			
	 */
	public void setDay(int day) throws IllegalArgumentException {
		if (day <= 31 && day >= 1) {
			this.day = day;
		} else {
			throw new IllegalArgumentException("Day must be "
												+ "between 1 and 31.");
		}
	}

	/**
	 * Returns the value of the year.
	 * 
	 * @return The year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Sets the year to a value greater than 0.
	 * 
	 * @param year The year to set
	 * 
	 * @throws IllegalArgumentException 
	 * 			If {@code year} is less than 0
	 */
	public void setYear(int year) throws IllegalArgumentException {
		if (year > 0) {
			this.year = year;
		} else { 
			throw new IllegalArgumentException("Year cannot be "
												+ "less than zero.");
		}
	}
	
	/**
	 * Tests if the {@code CustomDate} object's {@code year} variable is set to
	 * a value that represents a physical year that is a leap year.
	 *  
	 * @return {@code true} if {@code year} is a leap year, 
	 * 			{@code false} otherwise
	 */
	public boolean isLeapYear() {
		boolean isLeapYear = false;
		
		if (year % 4 == 0) {
			isLeapYear = true;
			if (year % 400 == 0 && year % 100 != 0) {
				isLeapYear = false;
			}
		}
		
		return isLeapYear;			
	}
	
	
	/**
	 * Advances this {@code CustomDate} one day.
	 */
	public void advanceOneDay() {
		final int ONE = 1;
		boolean lastDayOfMonth = false;
		boolean lastDayOfYear = false;
		
		/* 
		 * Determine amount of days in current month & if it's the last day of
		 * that month.
		 */
		switch (month) {
		
		case 2: 
			if ((day == 29 && this.isLeapYear())
					|| (day == 28 && !this.isLeapYear())) {
				lastDayOfMonth = true;
			}
			break;
			
		case 4: case 6: case 9: case 11:
			if (day == 30) {
				lastDayOfMonth = true;
			}
			break;
			
		default: 
			if (day == 31 && month == 12) {
				lastDayOfMonth = true;
				lastDayOfYear = true;
			} else if (day == 31) {
				lastDayOfMonth = true;
			}
			break;
		}
		
		/* 
		 * Advance day, month, and year if it's the last day of this month &&
		 * the last day of year. Only advance month and day if it's the last
		 * day of the month. Otherwise, only advance day. 
		 */
		if (lastDayOfMonth && lastDayOfYear) {
			setMonth(ONE);
			setDay(ONE);
			setYear(year + ONE);
		} else if (lastDayOfMonth) {
			setMonth(month + ONE);
			setDay(ONE);
		} else {
			setDay(day + ONE);
		}
	}
	
	/**
	 * Advances this {@code CustomDate} one week (seven days).
	 */
	public void advanceOneWeek() {
		for (int i = 0; i < 7; i++) {
			this.advanceOneDay();
		}
	}
	
	
	/**
	 * Compares two {@code CustomDate} objects chronologically. The comparison
	 * is based upon the Gregorian calendar and the intrinsic chronology of 
	 * its dates. 
	 * 
	 * @param obj The {@code CustomDate} object to be compared
	 * @return The value {@code 0} if the argument {@code CustomDate} is equal
	 * 			to this {@code CustomDate}; a value less than {@code 0} if this
	 * 			{@code CustomDate} is chronologically less than the 
	 * 			{@code CustomDate} argument; and a value greater than {@code 0}
	 * 			if this {@code CustomDate} is chronologically greater than the
	 * 			{@code CustomDate} argument
	 * 
	 * @throws IllegalArgumentException
	 * 			If the specified object is not an instance of the
	 * 			{@code CustomDate} class
	 * 			
	 * @throws NullPointerException
	 * 			If the specified object is {@code null}
	 */
	public int compareTo(Object obj) 
			throws IllegalArgumentException, NullPointerException {
		
		int result = -1;
		
		CustomDate temp;		
		if (obj != null) {
			if (obj instanceof CustomDate) {
				temp = (CustomDate) obj;
			} else {
				throw new IllegalArgumentException("Only two CustomDate objects"
													+ " can be compared");
			}			
		} else {
			throw new NullPointerException("Cannot pass a null object to "
											+ "CustomDate.compareTo(Object)");
		}
		
		if (this.equals(temp)) {
			result = 0;
		}
		
		if (this.year == temp.year && this.month == temp.month) {
			if (this.day > temp.day) {
				result = 1;
			} 
		} else if (this.year == temp.year) {
			if (this.month > temp.month) {
				result = 1;
			} 
		} else if (this.year > temp.year) {
			result = 1;
		}
		
		return result;
	}
	
	/**
	 * Checks for valid object initialization.
	 * 
	 * @throws IllegalArgumentException
	 * 			If the object that was created in the constructor does not
	 * 			represent a valid physical calendar date
	 */
	private void initialized() throws IllegalArgumentException  {
		if (!this.isValidDate()) {
			throw new IllegalArgumentException("This is not a valid date - "
					+ this.toString());
		}
	}
	
	/**
	 * Checks if the date provided in the constructor is a representation of a
	 * valid, physical calendar date.
	 * 
	 * @return {@code true} if the date exists, {@code false} otherwise
	 */
	private boolean isValidDate() {
		boolean result = true;
		
		switch (month) {
		
		/*
		 * If month is February, day can only have value of 1-29 during leap
		 * year, 1-28 otherwise.
		 */
		case 2: 
			if ((day > 29 && this.isLeapYear())
					|| (day > 28 && !this.isLeapYear())) {
				result = false;
			}
			break;
		
		/*
		 * Four months in the year, day can only have value 1-30
		 */
		case 4: case 6: case 9: case 11:
			if (day > 30) {
				result = false;
			}
			break;
		
		/*
		 * Otherwise, day can only be 1-31.
		 */
		default: 
			if (day > 31) {
				result = false;
			}
			break;
		}
		
		return result;
	}

	
	/** 
	 * Compares this {@code CustomDate} to the specified object. The result is
	 * {@code true} if and only if the argument is not {@code null} and is a 
	 * {@code CustomDate} object that represents the same physical calendar 
	 * date as this object.
	 * 
	 * @param obj The object to compare this {@code CustomDate} against
	 * 
	 * @return {@code true} if the given object represents a {@code CustomDate}
	 * 			 equivalent to this {@code CustomDate}, {@code false} otherwise
	 * 
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (obj != null && obj instanceof CustomDate) {
			CustomDate temp = (CustomDate) obj;
			if (temp.month == this.month && 
					temp.day == this.day &&
					temp.year == this.year) {	
				
				return true;
			}
		}
		
		return false;
	}

	
	/**
	 * Returns a string representation of the contents of the specified {@code 
	 * CustomDate} object. The string representation consists of a physical
	 * calendar date (i.e. month, day, year). Adjacent elements are separated
	 * by a forward slash {@code "/"} (in the format MM/DD/YYYY).
	 *  
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		String result = "";
		result += String.format("%02d", month) + "/";
		result += String.format("%02d", day) + "/";
		result += String.format("%04d", year);

		return result;
	}

}
