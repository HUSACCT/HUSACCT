package accessfield.b;

import accessfield.a.TheOwner;

public class OtherPackageA {
	
	private TheOwner owner = new TheOwner();

	public OtherPackageA(){
		String s = owner.theString;
	}
	
}
