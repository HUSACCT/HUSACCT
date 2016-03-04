package domain.indirect.violatingfrom;

import domain.indirect.intermediate.BackgroundService;

public class AccessInstanceVariableIndirect_MethodVar {
	private BackgroundService bckg;
	private String test;

	public AccessInstanceVariableIndirect_MethodVar(){
		bckg = new BackgroundService();
	}
		// Indirect access of a normal attribute 
		public String case1() {
			test = bckg.getServiceOne().name;
			return test;
		}
}
