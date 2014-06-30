package infrastructure.socialmedia.facebook.dao;

import domain.facebook.Link;

//Functional requirement 3.1.2
//Test case 72: Only class presentation.gui.facebook.LinkGUI may have a dependency with domain.facebook.Link
//Result: FALSE
public class LinkDAO {
	public LinkDAO(){
		//FR5.2
		System.out.println(Link.linkUrl);
	}
}