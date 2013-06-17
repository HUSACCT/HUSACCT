package domain.indirect.violatingfrom;

import domain.indirect.intermediate.BackgroundService;

public class CallStaticMethodIndirect_VarStaticMethod {
	
	private BackgroundService b;
	
	public CallStaticMethodIndirect_VarStaticMethod() {
	}

	// Indirect invocation of a static method
	@SuppressWarnings("static-access")
	public void case5(){
		String s = b.getServiceOne().getsName();
		System.out.println(s);
		
	}
}
