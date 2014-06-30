package domain.indirect.allowedfrom;

import domain.indirect.intermediate.BackgroundService;

public class AccessStaticVariableIndirectIndirect_VarVarVar {

	private BackgroundService bckg;
	private String test;

	public AccessStaticVariableIndirectIndirect_VarVarVar(){
		bckg = new BackgroundService();
	}
		//  Indirect-indirect access of a static attribute 
		@SuppressWarnings("static-access")
		public String case8() {
			test = bckg.serviceTwo.serviceOne.sName;
			return test;
		}
}