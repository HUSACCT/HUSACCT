package invocmethod.a;

public class SamePackageF {

	private TheType ref = new TheType();
	private Gui gui = new Gui();
	
	public SamePackageF(){
		ref.setGui(gui.getInstance());
	}
	
}
