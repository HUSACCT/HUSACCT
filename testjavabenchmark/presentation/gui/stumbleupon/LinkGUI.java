package presentation.gui.stumbleupon;

import domain.stumbleupon.Link;
//Functional requirement 3.2.2
//Test case 165: All classes in package presentation.gui.stumbleupon are not allowed to use in a not direct lower layer
//Result: TRUE
public class LinkGUI {
	public LinkGUI(){
		//FR5.2
		System.out.println(Link.linkUrl);
	}
}