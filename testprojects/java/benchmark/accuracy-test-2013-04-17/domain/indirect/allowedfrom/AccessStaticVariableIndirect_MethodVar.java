package domain.indirect.allowedfrom;

import domain.indirect.intermediate.BackgroundService;

public class AccessStaticVariableIndirect_MethodVar {
	private BackgroundService bckg;
	private String test;

	public AccessStaticVariableIndirect_MethodVar(){
		bckg = new BackgroundService();
	}
		//  Indirect access of a static attribute
		@SuppressWarnings("static-access")
		public String case5() {
			test = bckg.getServiceOne().sName;
			return test;
		}
}
