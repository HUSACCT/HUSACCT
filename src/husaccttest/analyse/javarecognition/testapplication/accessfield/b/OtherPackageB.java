package husaccttest.analyse.javarecognition.testapplication.accessfield.b;

import husaccttest.analyse.javarecognition.testapplication.accessfield.a.*;

public class OtherPackageB {
	
	private TheOwner owner = new TheOwner();

	public OtherPackageB(){
		owner.theString = "test";
	}
	
}
