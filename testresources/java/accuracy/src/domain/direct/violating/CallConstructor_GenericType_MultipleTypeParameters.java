package domain.direct.violating;

import java.util.HashMap;

import domain.direct.Base;
import technology.direct.dao.ProfileDAO;
import technology.direct.dao.UserDAO;

public class CallConstructor_GenericType_MultipleTypeParameters extends Base {
	public CallConstructor(){

		hashMap = new HashMap<ProfileDAO, UserDAO>();
	}
}