package shiftman.server;

public class Staff implements Comparable<Staff> {

	private String _givenName;
	private String _familyName;
	private String _fullName;

	public Staff(String givenName, String familyName) {
		_givenName = givenName;
		_familyName = familyName;
		_fullName = _givenName+" "+_familyName;
	}

	public String getGivenName() {
		return _givenName;
	}

	public String getFamilyName() {
		return _familyName;
	}

	public String getFullName() {
		return _fullName;
	}

	public int compareTo(Staff staffToCompare) {
		return _familyName.compareToIgnoreCase(staffToCompare._familyName);
	}
}
