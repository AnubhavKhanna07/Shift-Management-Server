package shiftman.server;

import java.util.ArrayList;
import java.util.List;

public class DayOfWeek {

	private String _day;
	private String _workingHoursStartTime = "null";
	private String _workingHoursEndTime = "null";
	private int _workingHoursStartTimeNum;
	private int _workingHoursEndTimeNum;
	private boolean _dayActive = false;
	private ShiftsGroup _shiftsScheduledToday = new ShiftsGroup();
	
	//The following field acts as a counter and keeps a track of the number of shifts
	//scheduled for the day.
	private int _noOfShifts = 0;


	public DayOfWeek(String day) {
		_day = day;
	}

	public String setWorkingHours(String workingHoursStartTime, String workingHoursEndTime) throws ShiftManServerException {
		String output = "";
		
		//The following two lines convert the working hours in the string format to integer format
		_workingHoursStartTimeNum = Integer.valueOf(workingHoursStartTime.substring(0, 2)+workingHoursStartTime.substring(3, 5));
		_workingHoursEndTimeNum = Integer.valueOf(workingHoursEndTime.substring(0, 2)+workingHoursEndTime.substring(3, 5));
		
		if (_workingHoursStartTimeNum >= _workingHoursEndTimeNum) {
			throw new ShiftManServerException("ERROR: Working hours start time is the same as of after the end time");
		}
		_workingHoursStartTime = workingHoursStartTime;
		_workingHoursEndTime = workingHoursEndTime;
		_dayActive = true;
		return output;
	}

	public String addShift(String dayOfWeek, String shiftStartTime, String shiftEndTime, String minimumWorkers) throws ShiftManServerException {
		String output = "";
		
		//The following two lines convert the shift start and end time from the string format to integer format.
		int shiftStartTimeNum = Integer.valueOf(shiftStartTime.substring(0, 2)+shiftStartTime.substring(3, 5));
		int shiftEndTimeNum = Integer.valueOf(shiftEndTime.substring(0, 2)+shiftEndTime.substring(3, 5));

		//In the following if block, it is checked if the shift to be added is within the working hours of the 
		//day. If not, then an exception is thrown.
		if (!(shiftStartTimeNum >= _workingHoursStartTimeNum && shiftEndTimeNum <= _workingHoursEndTimeNum)) {
			throw new ShiftManServerException("ERROR: Shift is not within working hours");
		}

		//If the shift is within the working hours, then the shift is added and the counter '_noOfShifts',
		//is increased by 1.
		output = _shiftsScheduledToday.addShift(dayOfWeek, shiftStartTime, shiftEndTime, minimumWorkers);
		if (output.equals("")) {
			_noOfShifts++;
		}
		return output;
	}

	public String assignStaff(String startTime, String endTime, Staff staffToAssign, boolean isManager) {
		String output = "";	
		output = _shiftsScheduledToday.assignStaff(startTime, endTime, staffToAssign, isManager);
		return output;
	}

	public List<Shift> shiftsWithoutManagers() {
		return _shiftsScheduledToday.shiftsWithoutManagers();
	}

	public List<Shift> understaffedShifts() {
		return _shiftsScheduledToday.understaffedShifts();
	}

	public List<Shift> overstaffedShifts() {
		return _shiftsScheduledToday.overstaffedShifts();
	}

	public List<String> getRosterForDay() {
		//If the number of shifts scheduled for the day is zero then an empty string list 
		//is returned as per the problem domain. Otherwise, the roster for the day is returned
		//as output with the working hours of the day having been added at the first 
		//position of the output list. Once the output of this method is passed to the 
		//Roster class, the shopname is added in the first position of the list to satisfy
		//the problem domain.
		List<String> output;
		if (_noOfShifts == 0) {
			output = new ArrayList<String>();
		}
		else {
			output = _shiftsScheduledToday.getRosterForShiftsGroup();
			output.add(0, _day+" "+_workingHoursStartTime+"-"+_workingHoursEndTime);
		}
		return output;
	}

	public List<Shift> getTodaysShiftsOfWorker(Staff workerToTest) {
		return _shiftsScheduledToday.getShiftsOfWorker(workerToTest);
	}

	public List<Shift> getShiftsManagedBy(String managerName) {
		return _shiftsScheduledToday.getShiftsManagedBy(managerName);
	}

	public String whichDayIsThis() {
		return _day;
	}

	public boolean isDayActive() {
		return _dayActive;
	}
}
