package domain.indirect.allowedfrom;

import domain.indirect.intermediate.BackgroundService;

public class AccessInstanceVariableIndirectIndirect_VarVarVar {
	private BackgroundService bckg;
	private String test;

	public AccessInstanceVariableIndirectIndirect_VarVarVar(){
		bckg = new BackgroundService();
	}
		// Indirect-indirect access of a normal attribute 
		public String case4() {
			test = bckg.serviceTwo.serviceOne.name;
			return test;
		}
}		
