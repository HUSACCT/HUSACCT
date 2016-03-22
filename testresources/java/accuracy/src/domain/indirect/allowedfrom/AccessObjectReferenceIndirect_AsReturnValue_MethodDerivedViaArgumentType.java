package domain.indirect.allowedfrom;

import domain.indirect.BaseIndirect;

public class AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaArgumentType {
	
	public String initializeProfileInformation(){
		String profile = "profiled";
		BaseIndirect baseClass = new BaseIndirect();
		Object o = baseClass.getMethodCorrectlyByDerivedArgumentType("tsja", profile, 3);
		return (String) o;
	}
}