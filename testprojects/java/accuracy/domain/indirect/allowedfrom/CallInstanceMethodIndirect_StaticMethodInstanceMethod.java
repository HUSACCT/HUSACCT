package domain.indirect.allowedfrom;

import domain.indirect.intermediate.BackgroundService;

public class CallInstanceMethodIndirect_StaticMethodInstanceMethod {
	
	public CallInstanceMethodIndirect_StaticMethodInstanceMethod() {
	}

	// Indirect invocation via static method
	public void case5(){
		String s = BackgroundService.getServiceOneviaStaticAttribute().getName();
		System.out.println(s);
		
	}
}
