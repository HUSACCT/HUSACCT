package domain.indirect.allowedfrom;

import domain.indirect.intermediate.BackgroundService;

public class AccessInstanceVariableIndirect_VarVarToString {
	private BackgroundService bckg;
	private String test;

	public AccessInstanceVariableIndirect_VarVarToString(){
		bckg = new BackgroundService();
	}
		// Indirect access of a normal attribute and with toString()
		public String case3() {
			test = bckg.serviceOne.day.toString();
			return test;
		}
}
