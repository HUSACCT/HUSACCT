package husaccttest.analyse.javarecognition.testapplication.accessfield.a;

public class SamePackageA {

	private TheOwner owner = new TheOwner();
	
	public SamePackageA(){
		String s = owner.theString;
	}
	
}
