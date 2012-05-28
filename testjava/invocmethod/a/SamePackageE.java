package invocmethod.a;

public class SamePackageE {

	private TheType ref = new TheType();
	private Gui gui = new Gui();
	
	public SamePackageE(){
		ref.setGui(gui);
	}
	
}
