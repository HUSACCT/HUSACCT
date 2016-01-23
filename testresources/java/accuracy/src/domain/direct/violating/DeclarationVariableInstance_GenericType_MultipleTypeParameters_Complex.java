package domain.direct.violating;

import technology.direct.dao.FriendsDAO;
import technology.direct.dao.ProfileDAO;
import husacct.common.dto.DependencyDTO;

import java.util.ArrayList;
import java.util.HashMap;

public class DeclarationVariableInstance_GenericType_MultipleTypeParameters_Complex {
	
	@SuppressWarnings("unused")
	private private HashMap<String, HashMap<ProfileDAO, ArrayList<FriendsDAO>>> complexHashMap;

	public String testUsageOfInstanceVariableDefinedByGenericTypeOneParameterType(){
		complexHashMap.clear();
		return "";
	}
}