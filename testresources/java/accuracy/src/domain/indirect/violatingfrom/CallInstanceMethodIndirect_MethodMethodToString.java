package domain.indirect.violatingfrom;

import domain.indirect.intermediate.BackgroundService;

public class CallInstanceMethodIndirect_MethodMethodToString {
	private BackgroundService bckg;
	private String test;
	
	public CallInstanceMethodIndirect_MethodMethodToString() {
		bckg = new BackgroundService();
	}

	public String Case4(){
		// Indirect invocation via normal method and with toString()
		test = bckg.getServiceOne().getDay().toString();
		return test;
	}
}
