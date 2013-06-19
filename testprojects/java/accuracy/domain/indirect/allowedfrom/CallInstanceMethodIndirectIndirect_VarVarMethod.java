package domain.indirect.allowedfrom;

import domain.indirect.intermediate.BackgroundService;

public class CallInstanceMethodIndirectIndirect_VarVarMethod {
	private BackgroundService bckg;
	private String test;
	
	public CallInstanceMethodIndirectIndirect_VarVarMethod() {
		bckg = new BackgroundService();
	}

	// Double indirect access via normal method 
	public String case6(){
			test = (String) bckg.serviceTwo.serviceOne.getName();
		return test;
	}
}
