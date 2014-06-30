package presentation.gui.orkut;

import domain.orkut.Community;

//Functional requirement 3.1.2
//Test case 71: Only class presentation.gui.orkut.CommunityGUI may have a dependency with domain.orkut.Community
//Result: TRUE
public class CommunityGUI {
	public CommunityGUI(){
		//FR5.2
		System.out.println(Community.communityName);
	}
}