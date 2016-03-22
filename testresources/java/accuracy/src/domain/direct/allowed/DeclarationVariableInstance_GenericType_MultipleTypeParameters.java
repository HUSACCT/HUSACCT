package domain.direct.allowed;

import technology.direct.dao.FriendsDAO;
import technology.direct.dao.ProfileDAO;

import java.util.HashMap;

public class DeclarationVariableInstance_GenericType_MultipleTypeParameters {
	
	@SuppressWarnings("unused")
	private HashMap<ProfileDAO, FriendsDAO> hashMap;

	public String testUsageOfInstanceVariableDefinedByGenericTypeOneParameterType(){
		hashMap.clear();
		return "";
	}
}