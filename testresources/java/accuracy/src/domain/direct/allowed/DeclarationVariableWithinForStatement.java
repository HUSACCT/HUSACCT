package domain.direct.allowed;

import technology.direct.dao.ProfileDAO;

public class DeclarationVariableWithinForStatement extends Base{
	
	public String getProfileInformation(){
		for (ProfileDAO pdao : profileDAOs) {
			String p = pdao.toString();
		}
		return "";
	}
}