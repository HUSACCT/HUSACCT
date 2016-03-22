package domain.direct.allowed;

import technology.direct.dao.FriendsDAO;
import technology.direct.dao.ProfileDAO;

import java.util.ArrayList;
import java.util.HashMap;

public class DeclarationVariableInstance_GenericType_MultipleTypeParameters_Complex {
	
	@SuppressWarnings("unused")
	private HashMap<String, HashMap<ProfileDAO, ArrayList<FriendsDAO>>> complexHashMap;

	public String testUsageOfInstanceVariableDefinedByGenericTypeOneParameterType(){
		complexHashMap.clear();
		return "";
	}
}