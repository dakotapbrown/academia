/**
 * The {@code PersonnelManager} can be used to manage an assortment of
 * objects that belong to the {@code Employee} class. This class is an 
 * array-based data structure.
 * 
 * @author Dakota Brown
 * @version CMSC 256, Section 001
 * @since February 24, 2019
 */
public class PersonnelManager {
	
	private Employee[] employeeList;
	private int numberOfEntries;
	private int capacity;
	private boolean initialized = false;
	private static final int ENTRY = 0;
	
	/**
	 * Constructs a {@code PersonnelManager} with null as its only element. 
	 */
	public PersonnelManager() {
		capacity = 1;
		employeeList = new Employee[capacity];
		initialized = true;
	}
	
	/**
	 * Returns the number of elements in this {@code PersonnelManager}.
	 * 
	 * @return The number of elements in this {@code PersonnelManager} 
	 */
	public int size() {
		checkInitialization();

		return numberOfEntries;
	}
	
	/**
	 * Adds an {@code Employee} to this {@code PersonnelManager}. If {@code
	 * anEmployee} equals an existing {@code Employee} object in this {@code
	 * PersonnelManager}, then it is replaced by {@code anEmployee}.
	 * 
	 * @param anEmployee The {@code Employee} to add
	 * @return {@code true} if {@code anEmployee} is successfully added to this
	 * 			{@code PersonnelManager}; {@code false} otherwise
	 */
	public boolean add(Employee anEmployee) {
		checkInitialization();
		
		boolean result = false;
		
		if (numberOfEntries == capacity) {
			doubleCapacity();
		}
		
		int index = find(anEmployee);
		
		if (index >= 0) {
			replace(index, anEmployee);
			result = true;
		} else {
			employeeList[numberOfEntries++] = anEmployee;
			result = true;
		}
		
				
		return result;
	}
	
	/**
	 * Removes specified {@code Employee} from this {@code PersonnelManager}. 
	 * 
	 * @param anEmployee The {@code Employee} to be removed
	 * @return The {@code Employee} removed from this {@code PersonnelManager}
	 * 			if it exists and the removal is successful; {@code null} 
	 * 			otherwise
	 */
	public Employee remove(Employee anEmployee) {
		checkInitialization();
		
		int index = find(anEmployee);
		
		Employee result = getEmployeeAtIndex(index);
		removeEmployeeAtIndex(index);
		
		return result;
	}
	
	/**
	 * Checks if this {@code PersonnelManager} contains the specified
	 * {@code Employee}.
	 * 
	 * @param anEmployee The {@code Employee} to check for
	 * @return {@code true} if this {@code PersonnelManager} contains the
	 * 			specified {@code Employee}; {@code false} otherwise
	 */
	public boolean contains(Employee anEmployee) {
		checkInitialization();
		
		boolean result = false;
		
		if (find(anEmployee) >= 0) {
			result = true;
		}
		
		return result;
	}
	
	/**
	 * Checks if this {@code PersonnelManager} is empty.
	 * 
	 * @return {@code true} if this {@code PersonnelManager} has no entries;
	 * 			{@code false} otherwise
	 */
	public boolean isEmpty() {
		checkInitialization();
		
		return numberOfEntries == 0;
	}
		
	/**
	 * Returns an array representation of this {@code PersonnelManager}.
	 * 
	 * @return An array representation of this {@code PersonnelManager}
	 */
	public Employee[] toArray() {
		checkInitialization();
		
		return copyOf(employeeList, size());
	}
	
	/**
	 * Checks if this {@code PersonnelManager} has been fully initialized.
	 * 
	 * @throws SecurityException
	 * 			If this {@code PersonnelManaer} hasn't been fully initialized
	 */
	private void checkInitialization() throws SecurityException {
		
		if (!initialized) {
			throw new SecurityException("PersonnnelManager not initialized");
		}
	}
	
	/**
	 * Retrieves the {@code Employee} at the specified index.
	 * 
	 * @param index The index of the desired {@code Employee}
	 * @return The {@code Employee} at the specified index; {@code null} if the
	 * 			specified index is -1 or this {@code PersonnelManager} is empty
	 */
	private Employee getEmployeeAtIndex(int index) {
		if (!isEmpty() && index >= 0) {
			return employeeList[index];
		}
		
		return null;
	}
	
	/**
	 * Replaces the {@code Employee} located at the specified index with the
	 * specified {@code Employee} ({@code anEmployee}).
	 * 
	 * @param index The index of the desired {@code Employee} to replace
	 * @param anEmployee The replacement {@code Employee} to place in the
	 * 					  specified index
	 */
	private void replace(int index, Employee anEmployee) {
		employeeList[index]	= anEmployee;		
	}

	/**
	 * Retrieves the index of the specified {@code Employee} in this {@code
	 * PersonnelManaager}.
	 * 
	 * @param anEmployee The {@code Employee} whose index is desired
	 * @return The index of the specified {@code Employee}, if it exists;
	 * 			{@code -1} otherwise
	 */
	private int find(Employee anEmployee) {
		if (anEmployee == null) {
			return -1;
		}
		
		int i = 0;
		for (Employee employee : employeeList) {
			if (employee != null
					&& employee
						.getLastName()
						.equals(anEmployee.getLastName())) {
				return i;
			}
			i++;
		}
		
		return -1;
	}

	/**
	 * Doubles the capacity of this {@code PersonnelManager}. 
	 */
	private void doubleCapacity() {
		capacity = 2 * capacity;
		employeeList = copyOf(employeeList, capacity);
	}

	/**
	 * Creates a copy (as an array) of this {@code PersonnelManager}'s 
	 * {@code employeeList} instance variable with the specified capacity.
	 * 
	 * @param employeeList The array to make a copy of
	 * @param capacity The length of the new array
	 * @return An array whose contents are equivalent to that of {@code
	 * 			employeeList} and whose length is the specified {@code
	 * 			capacity}
	 */
	private Employee[] copyOf(Employee[] employeeList, int capacity) {
		Employee[] newEmployeeList = new Employee[capacity];
		int length = Math.min(newEmployeeList.length, employeeList.length);
		
		for (int i = 0; i < length; i++) {
			newEmployeeList[i] = employeeList[i];
		}
		
		return newEmployeeList;
	}

	/**
	 * Removes the {@code Employee} at the specified index and replaces it with
	 * {@code null} if the specified index is the last element of {@code
	 * employeeList}; otherwise, removes the {@code Employee} at the specified
	 * index and moves each successive element in {@code employeeList} down one
	 * index.
	 * 
	 * @param index The index at which to remove the {@code Employee} 
	 * @return {@code null} if index is less than zero; otherwise returns the
	 * 			{@code Employee} that previously occupied the specified index
	 */
	private Employee removeEmployeeAtIndex(int index) {
		if (index == -1) {
			return null;
		}
		
		Employee employee = employeeList[index];
		
		if (index == employeeList.length - 1) {
			replace(index, null);
		} else {
			for (int i = index; i < numberOfEntries; i++) {
				replace(i, employeeList[i + 1]);
			}
		}
		
		numberOfEntries--;
		
		return employee;
	}


}
