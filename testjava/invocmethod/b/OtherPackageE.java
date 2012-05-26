package invocmethod.b;

import invocmethod.a.*;

public class OtherPackageE {

	private TheType ref = new TheType();
	
	public OtherPackageE(){
		ref.theMethod();
	}
	
}
