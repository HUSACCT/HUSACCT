package domain.indirect.allowedfrom;

import domain.indirect.BaseIndirect;
import domain.indirect.intermediate.ServiceTwo;

public class AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaHeuristic {
	ServiceTwo st = new ServiceTwo();
	
	public String initializeProfileInformation(){
		BaseIndirect baseClass = new BaseIndirect();
		Object o = baseClass.getMethodCorrectlyByHeuristic("tsja", st.getServiceOne(), 3);
		return o;
	}
}