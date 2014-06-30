package infrastructure.socialmedia.gowalla;

import domain.gowalla.Guide;
//Functional requirement 3.2.1
//Test case 161: The classes in package infrastructure.socialmedia.gowalla are not allowed to use modules in a higher layer
//Result: FALSE
public class GuideDAO{
	//FR5.5	
	private Guide guide;

	public GuideDAO(){
		//FR5.2
		System.out.println(guide.city);
	}
}