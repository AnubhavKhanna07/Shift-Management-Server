package shiftman.server;

import java.util.ArrayList;
import java.util.List;

public class Roster {

	private String _shopName;
	//_daysList will contain 7 objects representing the days of the week
	private List<DayOfWeek> _daysList = new ArrayList<DayOfWeek>(); 
	private StaffGroup _registeredStaffGroup = new StaffGroup();
	private StaffGroup _unassignedStaffGroup = new StaffGroup();

	public enum Days {
		MONDAY("Monday"), TUESDAY("Tuesday"), WEDNESDAY("Wednesday"), THURSDAY("Thursday"), FRIDAY("Friday"), SATURDAY("Saturday"), SUNDAY("Sunday");
		private String _day;
		private Days(String day) {
			this._day = day;
		}
	}

	public Roster(String shopName) {
		_shopName = shopName;

		// 7 DayOfWeek objects are created
		for (Days tempDay: Days.values()) {
			_daysList.add(new DayOfWeek(tempDay._day));
		}
	}

	public String setWorkingHours(String dayOfWeek, String startTime, String endTime) throws ShiftManServerException {
		String output = "";
		boolean isDayFound = false;

		//The following loop searches the list of weekdays for the day whose working hours are
		//desired to be set. Once the day is found, the working hours are then set. If the day
		//cannot be found then an exception is thrown
		for (DayOfWeek tempDay: _daysList) {
			if (tempDay.whichDayIsThis().equals(dayOfWeek)) {
				isDayFound = true;
				output = tempDay.setWorkingHours(startTime, endTime);
				break;
			}
		}
		if (isDayFound == false) {
			throw new ShiftManServerException("ERROR: Day given ("+dayOfWeek+") is invalid");
		}
		return output;
	}

	public String addShift(String dayOfWeek, String startTime, String endTime, String minimumWorkers) throws ShiftManServerException {
		String output = "";
		boolean isDayFound = false;

		//The following loop searches the list of weekdays for the day to which a new shift is
		//desired to be added. Once the day is found, the shift is added. If the day is found but the 
		//shop is not open on that day, then an exception is thrown. If the day cannot be found then 
		//too an exception is thrown.
		for (DayOfWeek tempDay: _daysList) {
			if (tempDay.whichDayIsThis().equals(dayOfWeek)) {
				isDayFound = true;
				if (tempDay.isDayActive() == false) {
					throw new ShiftManServerException("ERROR: The shop \""+_shopName+"\" is not open on ("+dayOfWeek+")");
				}
				output = tempDay.addShift(dayOfWeek, startTime, endTime, minimumWorkers);
				break;
			}
		}
		if (isDayFound == false) {
			throw new ShiftManServerException("ERROR: Day given ("+dayOfWeek+") is invalid");
		}
		return output;
	}

	public String registerStaff(String givenName, String familyName) {
		Staff staffToRegister = new Staff(givenName, familyName);
		String output = _registeredStaffGroup.addStaff(staffToRegister);
		
		//When a staff member is registered, he/she is also added to the group of unassigned staff as
		//immediately after registration, he/she remains unassigned to any shift.
		_unassignedStaffGroup.addStaff(staffToRegister);
		return output;
	}

	public String assignStaff(String dayOfWeek, String startTime, String endTime, String givenName, String familyName, boolean isManager) {
		String output = "";	
		
		//The Staff object, which represents the staff member with the provided name, is retrieved
		//from the group of registered staff.
		Staff staffToAssign = _registeredStaffGroup.whichStaffIsThis(givenName, familyName);
		
		//The following loop searches the list of weekdays for the day to which the staff has
		//to be assigned. Once the day is found, the staff member is assigned to the day.
		for (DayOfWeek tempDay: _daysList) {
			if (tempDay.whichDayIsThis().equals(dayOfWeek)) {
				output = tempDay.assignStaff(startTime, endTime, staffToAssign, isManager);
				break;
			}
		}
		
		//'staffToAssign' is the staff who also needs to be removed from the _unassignedStaffList after
		//he/she has been successfully assigned to a shift. The following lines of code delete the staff
		//member from the group of unassigned staff after he/she has been assigned to a shift.
		if (output.equals("")) { 
			_unassignedStaffGroup.deleteStaffFromList(staffToAssign);
		}
		return output;
	}

	public List<String> getRegisteredStaff() {
		return _registeredStaffGroup.getStaffInGroup();
	}

	public List<String> getUnassignedStaff() {
		return _unassignedStaffGroup.getStaffInGroup();
	}

	public List<String> shiftsWithoutManagers() {
		//The following loop retrieves the shifts without managers from all the days of
		//the week into a list which contains all such shifts from all days.
		List<Shift> noManagerShiftsList = new ArrayList<Shift>();
		for (DayOfWeek tempDay: _daysList) {
			noManagerShiftsList.addAll(tempDay.shiftsWithoutManagers());
		}

		//The following loop generates a string list containing all the shifts
		//without managers.
		List<String> output = new ArrayList<String>();
		for (Shift tempShift: noManagerShiftsList) {
			output.add(tempShift.toString());
		}
		return output;
	}

	public List<String> understaffedShifts() {
		//The following loop retrieves the understaffed shifts from all the days of
		//the week into a list which contains all such shifts from all days.
		List<Shift> understaffedShiftsList = new ArrayList<Shift>();
		for (DayOfWeek tempDay: _daysList) {
			understaffedShiftsList.addAll(tempDay.understaffedShifts());
		}

		//The following loop generates a string list containing all the understaffed shifts.
		List<String> output = new ArrayList<String>();
		for (Shift tempShift: understaffedShiftsList) {
			output.add(tempShift.toString());
		}
		return output;
	}

	public List<String> overstaffedShifts() {
		//The following loop retrieves the overstaffed shifts from all the days of
		//the week into a list which contains all such shifts from all days.
		List<Shift> overstaffedShiftsList = new ArrayList<Shift>();
		for (DayOfWeek tempDay: _daysList) {
			overstaffedShiftsList.addAll(tempDay.overstaffedShifts());
		}

		//The following loop generates a string list containing all the overstaffed shifts.
		List<String> output = new ArrayList<String>();
		for (Shift tempShift: overstaffedShiftsList) {
			output.add(tempShift.toString());
		}
		return output;
	}

	public List<String> getRosterForDay(String dayOfWeek) {
		//The following block of code cycles through the list of days and when
		//the day for which the roster is requested is found, that roster is appended
		//to the output list of this method.
		List<String> output = new ArrayList<String>();
		for (DayOfWeek tempDay: _daysList) {
			if (tempDay.whichDayIsThis().equals(dayOfWeek)) {
				output.addAll(tempDay.getRosterForDay());
				break;
			}
		}
		
		//If the number of shifts on the day is not zero then the shopname is added in the 
		//first position of the output list.
		if (output.isEmpty() == false) {
			output.add(0, _shopName);
		}
		return output;
	}

	public List<String> getRosterForWorker(String workerName) throws ShiftManServerException {
		//First, the Staff object which represents the worker with name 'workerName' is retrieved.
		//If no such object is found then an exception is thrown.
		Staff workerToTest = _registeredStaffGroup.whichStaffIsThis(workerName);
		if (workerToTest == null) {
			throw new ShiftManServerException("Worker with the name ("+workerName+") is not registered");
		}

		//The following block of code cycles through the list of days and in each
		//iteration appends the shifts of the worker on that day to a list which will finally contain 
		//the shifts of the worker from all days.
		List<Shift> shiftsOfWorker = new ArrayList<Shift>();
		for (DayOfWeek tempDay: _daysList) {
			shiftsOfWorker.addAll(tempDay.getTodaysShiftsOfWorker(workerToTest));
		}

		//The following loop generates a list of strings which represents all the shifts 
		//of the worker.
		List<String> output = new ArrayList<String>();
		for (Shift tempShift: shiftsOfWorker) {
			output.add(tempShift.toString());
		}
		
		//If the number of shifts of the worker is not zero then the name of the
		// worker is added in the first position of the output list.
		if (output.isEmpty() == false) {
			output.add(0, workerToTest.getFamilyName()+" "+workerToTest.getGivenName());
		}
		return output;
	}

	public List<String> getShiftsManagedBy(String managerName) {
		//First, the Staff object which represents the worker with name 'workerName' is retrieved.
		Staff managerToTest = _registeredStaffGroup.whichStaffIsThis(managerName);
		
		//The following block of code cycles through the list of days and in each
		//iteration appends the shifts managed by the given manager to a list which 
		//will finally contain such shifts from all days.
		List<Shift> shiftsOfManager = new ArrayList<Shift>();
		for (DayOfWeek tempDay: _daysList) {
			shiftsOfManager.addAll(tempDay.getShiftsManagedBy(managerName));
		}

		//The following loop generates a list of strings which represents all the shifts 
		//managed by the given manager.
		List<String> output = new ArrayList<String>();
		for (Shift tempShift: shiftsOfManager) {
			output.add(tempShift.toString());
		}
		
		//If the number of shifts managed by the given manager is not zero then the name of the
		//manager is added in the first position of the output list.
		if (output.isEmpty() == false) {
			output.add(0, managerToTest.getFamilyName()+", "+managerToTest.getGivenName());
		}
		return output;
	}
}
