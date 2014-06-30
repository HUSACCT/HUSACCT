package presentation.gui.orkut;

import domain.orkut.Application;

//Functional requirement 3.1.2
//Test case 67: Only class presentation.gui.orkut.ApplicationGUI may have a dependency with domain.orkut.Application 
//Result: TRUE
public class ApplicationGUI {
	public ApplicationGUI(){
		//FR5.1
		System.out.println(Application.getName());
	}
}