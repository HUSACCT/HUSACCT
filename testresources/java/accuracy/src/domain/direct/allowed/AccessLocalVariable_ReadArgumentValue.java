package domain.direct.allowed;

import technology.direct.dao.ProfileDAO;

public class AccessLocalVariable_ReadArgumentValue {
	
	public String getProfileInformation(String s, ProfileDAO profile, int i) {
		String pn = profile.name;
		return pn;
	}

}