package domain.indirect.allowedfrom;

import domain.indirect.intermediate.BackgroundService;

public class AccessInstanceVariableIndirectIndirect_MethodVarVar {
	private BackgroundService bckg;
	private String test;

	public AccessInstanceVariableIndirectIndirect_MethodVarVar(){
		bckg = new BackgroundService();
	}
		// Indirect-indirect access of a normal attribute 
		public String case4() {
			test = bckg.getServiceTwo().serviceOne.name;
			return test;
		}
}		
