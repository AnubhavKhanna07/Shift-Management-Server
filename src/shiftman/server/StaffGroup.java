package shiftman.server;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class StaffGroup {

	private List<Staff> _staffList = new ArrayList<Staff>();

	public String addStaff(Staff staffToAdd) {
		String output = "";
		_staffList.add(staffToAdd);
		return output;
	}

	public Staff whichStaffIsThis(String givenName, String familyName) {
		Staff outputStaff = null;

		for (Staff tempStaff: _staffList) {
			if ((tempStaff.getGivenName()).equals(givenName) && (tempStaff.getFamilyName()).equals(familyName)) {
				outputStaff = tempStaff;
				break;
			}
		}
		return outputStaff;
	}

	public Staff whichStaffIsThis(String workerName) {
		Staff outputStaff = null;

		for (Staff tempStaff: _staffList) {
			if (tempStaff.getFullName().equals(workerName)) {
				outputStaff = tempStaff;
				break;
			}
		}
		return outputStaff;
	}

	public void deleteStaffFromList(Staff staffToDelete) {
		_staffList.remove(staffToDelete);
	}

	public List<String> getStaffInGroup() {
		List<String> stringStaffList = new ArrayList<String>();
		//The list of staff members is sorted by family name
		Collections.sort(_staffList);
		//The following loop generates a string list containing the full names of all
		//the staff members in the group.
		for (Staff tempStaff: _staffList) {
			stringStaffList.add(tempStaff.getGivenName()+" "+tempStaff.getFamilyName());
		}
		return stringStaffList;
	}

	public boolean isWorkerInStaffGroup(Staff workerToTest) {
		return _staffList.contains(workerToTest);
	}
}
