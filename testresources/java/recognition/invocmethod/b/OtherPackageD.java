package invocmethod.b;

import invocmethod.a.TheType;

public class OtherPackageD {

	private TheType ref = new TheType();
	
	public OtherPackageD(){
		ref.theMethod();
	}
	
}
