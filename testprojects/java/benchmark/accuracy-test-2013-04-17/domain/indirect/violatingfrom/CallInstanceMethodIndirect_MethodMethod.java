package domain.indirect.violatingfrom;

import domain.indirect.intermediate.BackgroundService;

public class CallInstanceMethodIndirect_MethodMethod {
	private BackgroundService bckg;
	private String test;
	
	public CallInstanceMethodIndirect_MethodMethod() {
		bckg = new BackgroundService();
	}

	// Indirect access via normal method 
	public String case2(){
		test = bckg.getServiceOne().getName();
		return test;
	}
}
