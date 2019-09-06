package shiftman.server;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShiftsGroup {

	private List<Shift> _shiftsList = new ArrayList<Shift>();
	
	public String addShift(String dayOfWeek, String shiftStartTime, String shiftEndTime, String minimumWorkers) throws ShiftManServerException {
		String output = "";
		
		//The following two lines convert the shift start and end time from the string format to integer format
		int shiftStartTimeNum = Integer.valueOf(shiftStartTime.substring(0, 2)+shiftStartTime.substring(3, 5));
		int shiftEndTimeNum = Integer.valueOf(shiftEndTime.substring(0, 2)+shiftEndTime.substring(3, 5));

		//If the shift start time is the same as or after the end time then the following code throws 
		//an exception, otherwise the shift is added.
		if (shiftStartTimeNum >= shiftEndTimeNum) {
			throw new ShiftManServerException("ERROR: Shift start time is the same as of after the shift end time");
		}
		Shift tempShift = new Shift(dayOfWeek, shiftStartTime, shiftEndTime, minimumWorkers);
		_shiftsList.add(tempShift);
		return output;
	}

	public String assignStaff(String startTime, String endTime, Staff staffToAssign, boolean isManager) {
		//Initially the output string is made to store the error. Only if there is no error encountered
		//will the output string be blank as per the requirements of the problem domain.
		String output = "ERROR: Couldn't assign staff";	
		Shift shiftToAssignTo = null;
		boolean shiftFound = false;

		//The following loop searches for the shift to which the staff has to be assigned.
		for (Shift tempShift: _shiftsList) {
			if ((tempShift.getStartTime()).equals(startTime) && (tempShift.getEndTime()).equals(endTime)) {
				shiftToAssignTo = tempShift;
				shiftFound = true;
				break;
			}
		}
		if (shiftFound == true) {
			shiftToAssignTo.addStaffToShift(staffToAssign, isManager);
			output = "";
		}
		return output;
	}

	public List<Shift> shiftsWithoutManagers() {
		List<Shift> output = new ArrayList<Shift>();
		//The list of shifts is sorted by shift start time
		Collections.sort(_shiftsList);
		//The following loop generates a list containing all the shifts not having a manager
		for (Shift tempShift: _shiftsList) {
			if (tempShift.doesShiftHaveManager() == false) {
				output.add(tempShift);
			}
		}
		return output;
	}

	public List<Shift> understaffedShifts() {
		List<Shift> output = new ArrayList<Shift>();
		//The list of shifts is sorted by shift start time
		Collections.sort(_shiftsList);
		//The following loop generates a list containing all the understaffed shifts
		for (Shift tempShift: _shiftsList) {
			if (tempShift.isShiftUnderstaffed() == true) {
				output.add(tempShift);
			}
		}
		return output;
	}

	public List<Shift> overstaffedShifts() {
		List<Shift> output = new ArrayList<Shift>();
		//The list of shifts is sorted by shift start time
		Collections.sort(_shiftsList);
		//The following loop generates a list containing all the overstaffed shifts
		for (Shift tempShift: _shiftsList) {
			if (tempShift.isShiftOverstaffed() == true) {
				output.add(tempShift);
			}
		}
		return output;
	}

	public List<String> getRosterForShiftsGroup() {
		List<String> output = new ArrayList<String>();
		//The list of shifts is sorted by shift start time
		Collections.sort(_shiftsList);
		//The following loop generates a string list containing a string representation
		//of all the shifts in the group of shifts
		for (Shift tempShift: _shiftsList) {
			output.add(tempShift.toStringDetailed());
		}
		return output;
	}

	public List<Shift> getShiftsOfWorker(Staff workerToTest) {
		List<Shift> shiftsListOfWorker = new ArrayList<Shift>();
		//The following loop generates a list containing all the shifts of the given worker
		for (Shift tempShift: _shiftsList) {
			if (tempShift.isWorkerInShift(workerToTest) == true) {
				shiftsListOfWorker.add(tempShift);
			}
		}
		//The list of shifts is sorted by shift start time
		Collections.sort(shiftsListOfWorker);
		return shiftsListOfWorker;
	}

	public List<Shift> getShiftsManagedBy(String managerName) {
		List<Shift> shiftsListOfManager = new ArrayList<Shift>();
		//The following loop generates a list containing all the shifts 
		//managed by the given manager
		for (Shift tempShift: _shiftsList) {
			if (tempShift.isShiftManagedBy(managerName) == true) {
				shiftsListOfManager.add(tempShift);
			}
		}
		//The list of shifts is sorted by shift start time
		Collections.sort(shiftsListOfManager);
		return shiftsListOfManager;
	}
}
