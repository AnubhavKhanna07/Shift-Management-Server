package shiftman.server;

import java.util.List;

public class Shift implements Comparable<Shift> {

	private String _dayOfWeek;
	private String _startTime;
	private Integer _startTimeNum;
	private String _endTime;
	private Integer _endTimeNum;
	private int _minimumWorkersNum;
	private Staff _manager = null;
	private int _noOfWorkersInShift = 0;

	public Shift(String dayOfWeek, String startTime, String endTime, String minimumWorkers) {
		_dayOfWeek = dayOfWeek;
		_startTime = startTime;
		_startTimeNum = Integer.valueOf(startTime.substring(0, 2)+startTime.substring(3, 5));
		_endTime = endTime;
		_endTimeNum = Integer.valueOf(endTime.substring(0, 2)+endTime.substring(3, 5));
		_minimumWorkersNum = Integer.parseInt(minimumWorkers);
	}

	StaffGroup _workersInShift = new StaffGroup();

	public void addStaffToShift(Staff staffToAdd, boolean isManager) {
		//If the 'staffToAdd' is a worker then he/she is added to the group of workers in shift
		//Otherwise the 'staffToAdd' is a manager and is stored separately
		if (isManager == false) {
			_workersInShift.addStaff(staffToAdd);
			_noOfWorkersInShift++;
		}
		if (isManager == true) {
			_manager = staffToAdd;
		}
	}

	public boolean doesShiftHaveManager() {
		return !(_manager == null);
	}

	public boolean isShiftUnderstaffed() {
		return (_noOfWorkersInShift < _minimumWorkersNum);
	}

	public boolean isShiftOverstaffed() {
		return (_noOfWorkersInShift > _minimumWorkersNum);
	}

	@Override
	public String toString() {
		return (_dayOfWeek+"["+_startTime+"-"+_endTime+"]");
	}

	public String toStringDetailed() {
		//output contains day of week + manager details + worker(s)' details 
		String output;
		output = this.toString()+" ";

		if (_manager == null) {
			output += "[No manager assigned] ";
		}
		else {
			output += "Manager:"+_manager.getFamilyName()+", "+_manager.getGivenName()+" ";
		}

		if (_noOfWorkersInShift == 0) {
			output += "[No workers assigned]";
		}
		else {
			List<String> listOfWorkerNamesInShift = _workersInShift.getStaffInGroup();
			int lastIndexInNamesList = listOfWorkerNamesInShift.size() - 1;
			output += "[";
			for (String tempWorkerName: listOfWorkerNamesInShift) {
				//The next line makes sure that a comma is not added after the worker name
				//if it is the last name in the worker list
				if (listOfWorkerNamesInShift.indexOf(tempWorkerName) == lastIndexInNamesList) { 
					output += tempWorkerName;
				}
				else {
					output += tempWorkerName+", ";
				}
			}
			output += "]";
		}
		return output;
	}

	public boolean isWorkerInShift(Staff workerToTest) {
		return _workersInShift.isWorkerInStaffGroup(workerToTest);
	}

	public boolean isShiftManagedBy(String managerName) {
		if (_manager == null) {
			return false;
		}
		else {
			return _manager.getFullName().equalsIgnoreCase(managerName);
		}
	}

	@Override
	public int compareTo(Shift shiftToCompare) {
		return _startTimeNum.compareTo(shiftToCompare._startTimeNum);
	}

	public String getDayOfShift() {
		return _dayOfWeek;
	}

	public String getStartTime() {
		return _startTime;
	}

	public String getEndTime() {
		return _endTime;
	}

	public int getStartTimeNum() {
		return _startTimeNum;
	}

	public int getEndTimeNum() {
		return _endTimeNum;
	}
}
