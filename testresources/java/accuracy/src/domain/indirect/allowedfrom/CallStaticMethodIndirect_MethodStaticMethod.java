package domain.indirect.allowedfrom;

import domain.indirect.intermediate.BackgroundService;

public class CallStaticMethodIndirect_MethodStaticMethod {
	
	private BackgroundService b;
	
	public CallStaticMethodIndirect_MethodStaticMethod() {
	}

	// Indirect invocation of a static method
	@SuppressWarnings("static-access")
	public void case5(){
		String s = b.getServiceOne().getsName();
		System.out.println(s);
		
	}
}
