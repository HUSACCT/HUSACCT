package infrastructure.socialmedia.gowalla;

import domain.gowalla.CheckIn;
//Functional requirement 3.2.1
//Test case 161: The classes in package infrastructure.socialmedia.gowalla are not allowed to use modules in a higher layer
//Result: FALSE
public class CheckInDAO{
	//FR5.5
	private CheckIn checkin;

	public CheckInDAO(){
		//FR5.1
		System.out.println(checkin.getType());
	}
}