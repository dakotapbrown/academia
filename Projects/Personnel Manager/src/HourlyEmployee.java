
/**
 * The {@code HourlyEmployee} class represents an hourly employee that has a
 * and an hourly wage. This class is subclass of the {@code Employee} class.
 * 
 * @author Dakota Brown
 * @version CMSC 256, Section 001
 * @since February 24, 2019
 */
public class HourlyEmployee extends Employee {
	
	private static final int FORTY_HOURS = 40;
	private static final double OVERTIME_RATE = 1.5;
	
	public HourlyEmployee(String firstName,
						  String lastName, 
						  double hourlyWage) {
		super(firstName, lastName, hourlyWage);
	} 
     
	/**
	 * calculates the weekly pay for this employee
	 * 
	 * 
	 * @param hours	the number of hours worked for the week
	 * @return	the pay amount based on <code>hours</code>
	 */
	@Override
	public double computePay(double hours) {
		double result = 0;
		
		if (hours <= 40) {
			result = hours * this.getWage();
		} else {
			result = hours * this.getWage();
			result += (FORTY_HOURS - hours) * (this.getWage() * OVERTIME_RATE);
		}
		
		return result;
	}

	@Override
	public String toString() {
		final int MAX_CHARACTERS = 40;	// Max characters allowed for string
		
		StringBuilder strBuild = new StringBuilder();		
		strBuild.append(this.getName());
		strBuild.ensureCapacity(40);		
		String wage = String.format("%s%.2f%s",
				"$",
				this.getWage(),
				"/hour");
		int whitespaceAmount = MAX_CHARACTERS - strBuild.length()
								- wage.length();
		
		char[] whitespace = new char[whitespaceAmount];
		for (int i = 0; i < whitespace.length; i++) {
			whitespace[i] = ' ';
		}
		
		int lastIndexOfName = this.getName().length();
		
		strBuild.insert(lastIndexOfName, whitespace);
		strBuild.insert(whitespaceAmount + lastIndexOfName, wage);
		
		String result = strBuild.toString();
		return result;
	}
	
	

}
