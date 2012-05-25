package invocmethod.a;

public class SamePackageG {

	private TheType ref = new TheType();
	
	public SamePackageG(){
		ref.setGui(new Gui().getInstance());
	}
	
}
