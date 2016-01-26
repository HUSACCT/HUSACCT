package domain.direct.allowed;

import java.util.ArrayList;
import technology.direct.dao.ProfileDAO;

public class DeclarationVariableStatic_GenericType_OneTypeParameter {
	
	@SuppressWarnings("unused")
	private static ArrayList<ProfileDAO> arrayList;

	public static String testUsageOfInstanceVariableDefinedByGenericTypeOneParameterType(){
		arrayList.clear();
		return "";
	}
}