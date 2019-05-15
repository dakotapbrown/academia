
/**
 * The {@code SalariedEmployee} class represents a salaried employee with 
 * a name and an annual salary. This class is subclass of the {@code Employee}
 * class.
 * 
 * @author Dakota Brown
 * @version CMSC 256, Section 001
 * @since February 24, 2019
 */
public class SalariedEmployee extends Employee {
	
	private static final int FORTY_HOURS = 40;
	
	public SalariedEmployee(String firstName, String lastName, double annualSalary) {
		super(firstName, lastName, getHourlyWage(annualSalary));
		
	}
	
	/**
	 * @param annualSalary
	 */
	public void setAnnualSalary(double annualSalary) {
		this.setWage(getHourlyWage(annualSalary));
	}
	
	/**
	 * Returns the annual salary of this {@code SalariedEmployee}.
	 * 
	 * @return The annual salary of this {@code SalariedEmployee}
	 */
	public double getAnnualSalary() {
		
		return this.computePay(FORTY_HOURS) * 52;
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
		
		return this.getWage() * FORTY_HOURS;
	}
	
	@Override
	public String toString() {
		final int MAX_CHARACTERS = 40;	// Max characters allowed for string
	
		StringBuilder strBuild = new StringBuilder();		
		strBuild.append(this.getName());
		strBuild.ensureCapacity(40);		
		String wage = String.format("%s%.2f%s",
				"$",
				this.getAnnualSalary(),
				"/year");
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

	/**
	 * Returns the hourly wage of this {@code SalariedEmployee} based on the
	 * {@code annualSalary} and a 40 hour work-week, 52 weeks a year.
	 * 
	 * @param annualSalary The annual salary of this {@code SalariedEmployee}
	 * @return The hourly wage
	 */
	private static double getHourlyWage(double annualSalary) {
		
		return (annualSalary / 52.0) / 40.0;
	}

}
