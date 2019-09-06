package shiftman.server;

import java.util.List;
import java.util.ArrayList;

public class ShiftManServer implements ShiftMan {

	Roster _roster = null;

	public String newRoster(String shopName) {
		String output = "";
		try {
			if (shopName.equals("") || shopName == null) {
				throw new ShiftManServerException("ERROR: Shop name has not been provided");
			}
			else {
				_roster = new Roster(shopName);
			}
		}
		//If an exception is caught, the error message is made the 
		//output of the method.
		catch(ShiftManServerException e) {
			output = e.getMessage();
		}
		return output;
	}

	public String setWorkingHours(String dayOfWeek, String startTime, String endTime) {
		String output = "";
		try {
			_roster.setWorkingHours(dayOfWeek, startTime, endTime);
		} 
		//If an exception is caught, the error message is made the 
		//output of the method.
		catch(ShiftManServerException e) {
			output = e.getMessage();
		}
		return output;
	}

	public String addShift(String dayOfWeek, String startTime, String endTime, String minimumWorkers) {
		String output = "";
		try {
			output = _roster.addShift(dayOfWeek, startTime, endTime, minimumWorkers);
		}
		//If an exception is caught, the error message is made the 
		//output of the method.
		catch(ShiftManServerException e) {
			output = e.getMessage();
		}
		return output;
	}

	public String registerStaff(String givenName, String familyName) {
		String output = "";
		output = _roster.registerStaff(givenName, familyName);
		return output;
	}

	public String assignStaff(String dayOfWeek, String startTime, String endTime, String givenName, String familyName, boolean isManager) {
		String output = "";	
		output = _roster.assignStaff(dayOfWeek, startTime, endTime, givenName, familyName, isManager);
		return output;
	}

	public List<String> getRegisteredStaff() {
		try {
			if (_roster != null) {
				return _roster.getRegisteredStaff();
			}
			else {
				throw new ShiftManServerException("ERROR: A roster has not been created");
			}
		}
		//If an exception is caught, a new string list is created and the error
		//message is appended.
		catch(ShiftManServerException e) {
			List<String> output = new ArrayList<String>();
			output.add(e.getMessage());
			return output;
		}
	}

	public List<String> getUnassignedStaff() {
		return _roster.getUnassignedStaff();
	}

	public List<String> shiftsWithoutManagers() {
		return _roster.shiftsWithoutManagers();
	}

	public List<String> understaffedShifts() {
		return _roster.understaffedShifts();
	}

	public List<String> overstaffedShifts() {
		return _roster.overstaffedShifts();
	}

	public List<String> getRosterForDay(String dayOfWeek) {
		return _roster.getRosterForDay(dayOfWeek);
	}

	public List<String> getRosterForWorker(String workerName) {
		List<String> output;
		try {
			output = _roster.getRosterForWorker(workerName);
		}
		//If an exception is caught, a new string list is created and the error
		//message is appended.
		catch(ShiftManServerException e) {
			output = new ArrayList<String>();
			output.add(e.getMessage());
		}
		return output;
	}

	public List<String> getShiftsManagedBy(String managerName) {
		return _roster.getShiftsManagedBy(managerName);
	}

	public String reportRosterIssues() {
		String output = "";
		return output;
	}

	public String displayRoster() {
		String output = "";
		return output;
	}
}
