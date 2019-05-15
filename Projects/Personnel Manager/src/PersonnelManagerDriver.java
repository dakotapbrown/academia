import java.io.*;
import java.util.*;

/**
 * The {@code PersonnelManagerDriver} class serves to read and write files dictating the pay
 * and employment of a companies employees.
 *
 * @author Dakota Brown
 * @version CMSC 256, Section 001
 * @since February 24, 2019
 */
public final class PersonnelManagerDriver {
	// prevent inheritance

	private static int loopCount;
	private static Scanner read;
	private static PrintWriter out;
	private static PersonnelManager employees;
	private static boolean exceptionThrown;
	private static final ArrayList<String[]> hours = new ArrayList<>();

	// prevent instances of this class
	private PersonnelManagerDriver() {}

	public static void main(String[] args) {


		printHeading();

		employees = new PersonnelManager();
		loopCount = 1;
		String file = "EmployeesIn.dat";
		boolean done = false;
		File inFile = null;

		do {
			try {
				inFile = new File(file);
				read = new Scanner(inFile);
				readFile();
				exceptionThrown = false;
			} catch (FileNotFoundException fnfe) {
				exceptionThrown = true;
				System.out.printf("%n%s%n", fnfe.getMessage());
				file = readUserInput();
			} catch (EOFException eofe) {
				exceptionThrown = true;
				System.out.printf("%n%s: %s%n",
								  eofe.getMessage(),
								  inFile.getName());
				file = readUserInput();
			} finally {
				if (read.ioException() == null) {
					read.close();
				}
			}

			if (!exceptionThrown) {
				loopCount++;
				if (loopCount == 2) {
					file = "Updates.dat";
				} else if (loopCount == 3) {
					file = "HoursWorked.dat";
				} else {
					done = true;
				}
			}



		} while (!done);

		try {
			out = new PrintWriter(new File("WeeklyPay.txt"));
			writeWeeklyPay();
		} catch (FileNotFoundException fnfe) {
			System.out.println(fnfe.getMessage());
			System.exit(0);
		} finally {
			if (!out.checkError()) {
				out.close();
			}
		}

		try {
			out = new PrintWriter(new File("EmployeesOut.dat"));
			writeEmployeesOut();
		} catch (FileNotFoundException fnfe) {
			System.out.println(fnfe.getMessage());
			System.exit(0);
		} finally {
			if (!out.checkError()) {
				out.close();
			}
		}
	}

	private static String readUserInput() {
		// resource 'in' may be reused, therefore can't be closed
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		System.out.printf("Please specify a new file: ");
		String result = in.nextLine();

		return result;
	}

	private static void printHeading() {
		System.out.println("Dakota Brown");
	    System.out.println("Project 2 - Personnel");
	    System.out.println("CMSC 256 - Section 001");
	    System.out.println("Spring 2018\n\n\n");
	}

	private static Employee delete(String lastName) {
		Employee employee = new HourlyEmployee(null, lastName, 0);

		return employees.remove(employee);
	}

	private static void readFile() throws EOFException {
		boolean empty = true;
		int lineCount = 1;

		while (read.hasNextLine()) {
			empty = false;

			String line = read.nextLine();
			/*
			 * The following regular expression matches a string with the
			 * following criteria:
			 *
			 * At the start of the line, there may or may not be leading
			 * whitespace, followed by a last name and first name (words),
			 * separated by a comma and at least one space (non-word
			 * characters). Following must be one or more whitespace
			 * characters, the 's' or 'h' character (upper- or lowercase),
			 * followed again by one or more whitespace characters. The last
			 * expected entry must either be an integer or floating point
			 * number (with or without leading numbers). There may or may not
			 * be additional whitespace before the new line character.
			 *
			 * Ex. "Harris, Joan  h  15.00"
			 */
			String employeesInRegex =
					"^\\s*?\\w+\\W+\\w+\\s+[HhSs]\\s+"
					+ "(\\d+|\\d*?\\.\\d+?)\\s*?$";
			/*
			 * The following regular expression matches a string with the
			 * following criteria:
			 *
			 * At start of line, there may or may not be leading whitespace,
			 * followed by the 'd' character (upper- or lowercase). One or more
			 * whitespace characters shall follow before a last name (word) is
			 * encountered. There may or may not be additional whitespace
			 * before the end of the line.
			 *
			 * OR
			 *
			 * At start of line, there may or may not be leading whitespace,
			 * followed by the 'r' character (upper- or lowercase). One or more
			 * whitespace characters shall follow before an integer or floating
			 * point number is encountered. There may or may not be additional
			 * whitespace before the end of the line.
			 *
			 * OR
			 *
			 * At the start of the line, there may or may not be leading
			 * whitespace, followed by the 'n' character (upper- or lowercase).
			 * After at least one whitespace character, a last name and first
			 * name (words), separated by a comma and at least one space
			 * (non-word characters). Following must be one or more whitespace
			 * characters, the 's' or 'h' character (upper- or lowercase),
			 * followed again by one or more whitespace characters. The last
			 * expected entry must either be an integer or floating point
			 * number (with or without leading numbers). There may or may not
			 * be additional whitespace before the new line character.
			 *
			 * Ex. "d Kinsey"
			 * Ex. "r  5"
			 * Ex. "n  Harris, Joan  h  15.00"
			 */
			String updatesRegex =
					"(^\\s*?[Dd]\\s+\\w+$)|(^\\s*?[Rr]\\s+(\\d+|\\d*?\\.\\d+?)"
					+ "$)|(^\\s*?[Nn]\\s+\\w+\\W+\\w+\\s+[HhSs]\\s+(\\d+|\\d*?"
					+ "\\.\\d+?)\\s*?$)";
			/*
			 * The following regular expression matches a string with the
			 * following criteria:
			 *
			 * At the start of the line, there may or may not be leading
			 * whitespace, followed by a last name (word characters). At least
			 * one or more whitespace characters separate either an integer or
			 * floating point number (with or without leading numbers). There
			 * may or may not be trailing whitespace before the end of the line.
			 *
			 * Ex. "Kinsey 65.00"
			 */
			String hoursWorkedRegex =
					"^\\s*?\\w+\\s+(\\d+|\\d*?\\.\\d+?)\\s*?$";
			/*
			 * Matches an empty line.
			 */
			String emptyLineRegex = "^\\s*$";

			if (line.matches(employeesInRegex)) {
				// file encountered is EmployessIn.dat
				parseEmployeesIn(line);
			} else if (line.matches(updatesRegex)) {
				// file encountered is Updates.dat
				parseUpdates(line);
			} else if (line.matches(hoursWorkedRegex)) {
				// file encountered is HoursWorked.dat
				parseHoursWorked(line);
			} else if (line.matches(emptyLineRegex)) {
				// line did not contain anything but whitespace; file is
				// technically empty
				empty = true;
			} else {
				// the line did not match the expected format, therefore is
				// unreadable
				System.out.printf("Command was not recognized; <line #%d: "
								  + "\"%s\">%n",
								  lineCount,
								  line);
			}
			lineCount++;
		}

		if (empty) {
			throw new EOFException("The specified file does not coantain any data");
		}

	}

	private static void parseEmployeesIn(String line) {
		line = line.trim();
		/*
		 *  Split the line into tokens that are not one or more non-word
		 *  character(s) (excluding periods) consecutively
		 */
		String[] lineToken = line.split("[^\\w.]+");

		String lastName = lineToken[0];
		String firstName = lineToken[1];
		String identifier = lineToken[2].toLowerCase();
		double wage = Double.parseDouble(lineToken[3]);

		if (identifier.equals("h")) {
			// employee is hourly employee
			employees.add(new HourlyEmployee(firstName,
											 lastName,
											 wage));
		} else if (identifier.equals("s")) {
			// employee is salaried employee
			employees.add(new SalariedEmployee(firstName,
					 						   lastName,
					 						   wage));
		}
	}


	private static void parseUpdates(String line) {
		line = line.trim();
		/*
		 *  Split the line into tokens that are not one or more non-word
		 *  character(s) (excluding periods) consecutively
		 */
		String[] lineToken = line.split("[^\\w.]+");

		String command = lineToken[0];

		if (command.equals("n")) {
			// add new employee
			String lastName = lineToken[1];
			String firstName = lineToken[2];
			double wage = Double.parseDouble(lineToken[4]);
			String identifier = lineToken[3].toLowerCase();

			if (identifier.equals("h")) {
			// employee is hourly employee
			employees.add(new HourlyEmployee(firstName,
											 lastName,
											 wage));
			} else if (identifier.equals("s")) {
			// employee is salaried employee
			employees.add(new SalariedEmployee(firstName,
					 						   lastName,
					 						   wage));
			}

			// inform user of update
			System.out.printf("New Employee added:  %s%n",
							  employees
							  	.toArray()[employees.size() - 1]
							  	.getName());

		} else if (command.equals("r")) {
			// raise employee wages
			double payRateIncrease = Double.parseDouble(lineToken[1]);
			System.out.printf("New Wages: %n");
			for (Employee employee : employees.toArray()) {
				if (employee != null) {
					employee.raiseWages(payRateIncrease);
					System.out.printf("\t%s%n", employee.toString());
				}
			}
		} else if (command.equals("d")) {
			// delete specified employee
			Employee deletedEmployee = delete(lineToken[1]);
			if (deletedEmployee == null) {
				System.out.printf("Attempted to delete employee with "
								  + "last name \"%s.\" Employee did "
								  + "not exist.%n", lineToken[1]);
			} else {
				System.out.printf("Deleted Employee: %s%n",
								  deletedEmployee.getName());
			}
		}
	}

	private static void parseHoursWorked(String line) {
		line = line.trim();
		/*
		*  Split the line into tokens that are not one or more non-word
		*  character(s) (excluding periods) consecutively
		*/
		String[] lineToken = line.split("[^\\w.]+");

		hours.add(lineToken);
	}

	private static void writeWeeklyPay() {
		// prints out a formatted file of employee weekly pay
		out.printf("Paycheck amount:%n");
		double total = 0;
		for (Employee temp : employees.toArray()) {
			for (String[] element : hours) {
				if (element != null
						&& temp.getLastName().equals(element[0])) {
					double pay = temp.computePay(Double.parseDouble(element[1]));
					total += pay;
					String payFormatted = String.format("$%.2f", pay);
					out.printf("\t%-20s%10s%n", temp.getName(), payFormatted);
				}
			}
		}
		out.printf("\t%30s%n", "---------");
		String totalFormatted = String.format("$%.2f", total);
		out.printf("\t%-20s%10s%n", "Total", totalFormatted);
	}

	private static void writeEmployeesOut() {
		// prints out a formatted file of the employees still employed
		for (Employee temp : employees.toArray()) {
			if (temp instanceof HourlyEmployee) {
				HourlyEmployee tempHourlyEmp = (HourlyEmployee) temp;
				out.printf("%s  h  %.2f%n",
						   tempHourlyEmp.getName(),
					   	   tempHourlyEmp.getWage());
			} else if (temp instanceof SalariedEmployee) {
				SalariedEmployee tempSalariedEmp = (SalariedEmployee) temp;
				out.printf("%s  s  %.2f%n",
						   tempSalariedEmp.getName(),
						   tempSalariedEmp.getAnnualSalary());
			}
		}
	}
}
