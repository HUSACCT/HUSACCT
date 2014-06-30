package invocmethod.a;

public class SamePackageI {

	private TheType ref = new TheType();
	
	public SamePackageI(){
		ref.getGui().getInstance().notify();
	}
	
}
