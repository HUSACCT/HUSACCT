package domain.direct.allowed;

import technology.direct.dao.ProfileDAO;

public class AccessInstanceVariable_SetArgumentValue {
	
	ProfileDAO profile = null;

	// Tests if: 1) the type of argument profile is found; 2) thereafter the right method is selected; 3) the return value of this method is found.
	public String theMethod(String s, int i) {
		String pn = getProfileInformation(profile, "test", 4).checkinValue.name;
		return pn;
	}
	
	public String getProfileInformation(String s, ProfileDAO profile, int i) {
		String pn = s;
		return pn;
	}

	public ProfileDAO getProfileInformation(ProfileDAO profile, String s, int i) {
		return profile;
	}
}