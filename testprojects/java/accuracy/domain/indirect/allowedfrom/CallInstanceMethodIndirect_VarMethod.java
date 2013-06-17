package domain.indirect.allowedfrom;

import domain.indirect.intermediate.BackgroundService;

public class CallInstanceMethodIndirect_VarMethod {
	private BackgroundService bckg;
	private String test;
	
	public CallInstanceMethodIndirect_VarMethod() {
		bckg = new BackgroundService();
	}

	// Indirect access via normal method 
	public String case2(){
		test = bckg.serviceOne.getName();
		return test;
	}
}
