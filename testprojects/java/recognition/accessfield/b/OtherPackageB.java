package accessfield.b;

import accessfield.a.*;

public class OtherPackageB {
	
	private TheOwner owner = new TheOwner();

	public OtherPackageB(){
		owner.theString = "test";
	}
	
}
