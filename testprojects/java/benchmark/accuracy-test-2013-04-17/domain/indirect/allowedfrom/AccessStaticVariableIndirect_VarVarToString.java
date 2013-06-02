package domain.indirect.allowedfrom;

import domain.indirect.intermediate.BackgroundService;

public class AccessStaticVariableIndirect_VarVarToString {
	private BackgroundService bckg;
	private String test;

	public AccessStaticVariableIndirect_VarVarToString(){
	}

	// Indirect access via static attribute and toString
		@SuppressWarnings("static-access")
		public String case7() {
			test = bckg.serviceOne.sName.toString();
			return test;
		}
}
