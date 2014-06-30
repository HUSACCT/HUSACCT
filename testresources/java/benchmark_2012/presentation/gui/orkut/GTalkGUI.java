package presentation.gui.orkut;

import domain.orkut.GTalk;

//Functional requirement 3.1.2
//Test case 73: Only class presentation.gui.orkut.GTalkGUI may have a dependency with domain.orkut.GTalk
//Result: TRUE
public class GTalkGUI {
	//FR5.5
	private GTalk gtalk;
	
	public GTalkGUI(){
		//FR5.2
		System.out.println(gtalk.status);
	}
}