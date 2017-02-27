package domain.direct.violating;

import technology.direct.dao.ProfileDAO;

public class Statement_LambdaExpression{

	private ProfileDAO profileDao;

	public Statement_LambdaExpression(ProfileDAO[] profileDaoArray){
		return profileDaoArray -> {profileDaoArray.checkinValue.name = "hu";};
	}
}