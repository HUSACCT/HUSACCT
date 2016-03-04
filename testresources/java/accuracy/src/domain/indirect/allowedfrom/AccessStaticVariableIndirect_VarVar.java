package domain.indirect.allowedfrom;

import domain.indirect.intermediate.BackgroundService;

public class AccessStaticVariableIndirect_VarVar {
	private BackgroundService bckg;
	private String test;

	public AccessStaticVariableIndirect_VarVar(){
		bckg = new BackgroundService();
	}
		//  Indirect access of a static attribute
		@SuppressWarnings("static-access")
		public String case5() {
			test = bckg.serviceOne.sName;
			return test;
		}
}
