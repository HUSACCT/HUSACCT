package domain.direct.allowed;

import technology.direct.dao.ProfileDAO;

public class AccessInstanceVariable_SetArgumentValue {
	
	ProfileDAO profile;

	public String theMethod(String s, int i) {
		String pn = getProfileInformation(profile, "test", 4).checkinValue;
		return pn;
	}
	
	public String getProfileInformation(String s, ProfileDAO profile, int i) {
		String pn = s;
		return pn;
	}

	public ProfileDAO getProfileInformation(ProfileDAO profile, String s, int i) {
		String pn = s;
		return pn;
	}


}