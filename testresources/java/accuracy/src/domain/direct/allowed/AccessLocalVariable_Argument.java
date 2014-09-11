package domain.direct.allowed;

import technology.direct.dao.ProfileDAO;

public class AccessLocalVariable_Argument {
	
	public String getProfileInformation(String s, ProfileDAO profile, int i) {
		String s = profile.name;
		return s;
	}
}