package domain.indirect.violatingfrom;

import domain.indirect.intermediate.BackgroundService;

public class CallInstanceMethodIndirect_MethodMethodViaConstructor {
	
	private String test;
	
	public CallInstanceMethodIndirect_MethodMethodViaConstructor() {
	}

	public String case1(){
		test = new BackgroundService().getServiceOne().getName();
		return test;
	}
}
