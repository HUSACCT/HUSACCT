package domain.indirect.allowedfrom;

import domain.indirect.intermediate.BackgroundService;

public class AccessStaticVariableIndirectIndirect_MethodVarVar {

	private BackgroundService bckg;
	private String test;

	public AccessStaticVariableIndirectIndirect_MethodVarVar(){
		bckg = new BackgroundService();
	}
		//  Indirect-indirect access of a static attribute 
		@SuppressWarnings("static-access")
		public String case8() {
			test = bckg.getServiceTwo().serviceOne.sName;
			return test;
		}
}