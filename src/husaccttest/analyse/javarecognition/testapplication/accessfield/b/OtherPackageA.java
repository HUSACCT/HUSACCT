package husaccttest.analyse.javarecognition.testapplication.accessfield.b;

import husaccttest.analyse.javarecognition.testapplication.accessfield.a.TheOwner;

public class OtherPackageA {
	
	private TheOwner owner = new TheOwner();

	public OtherPackageA(){
		String s = owner.theString;
	}
	
}
