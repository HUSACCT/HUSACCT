package domain.indirect.allowedfrom;

import domain.indirect.intermediate.BackgroundService;

public class AccessInstanceVariableIndirect_VarVar {
	private BackgroundService bckg;
	private String test;

	public AccessInstanceVariableIndirect_VarVar(){
		bckg = new BackgroundService();
	}
		// Indirect access of a normal attribute 
		public String case1() {
			test = bckg.serviceOne.name;
			return test;
		}
}
