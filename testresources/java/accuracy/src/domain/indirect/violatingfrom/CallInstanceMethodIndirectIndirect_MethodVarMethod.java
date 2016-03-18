package domain.indirect.violatingfrom;

import domain.indirect.intermediate.BackgroundService;

public class CallInstanceMethodIndirectIndirect_MethodVarMethod {
	private BackgroundService bckg;
	private String test;
	
	public CallInstanceMethodIndirectIndirect_MethodVarMethod() {
		bckg = new BackgroundService();
	}

	// Double indirect access via normal method 
	public String case6(){
			test = (String) bckg.getServiceTwo().serviceOne.getName();
		return test;
	}
}
