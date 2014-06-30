package infrastructure.socialmedia.gowalla;

import domain.gowalla.GoWallaException;
//Functional requirement 3.2.1
//Test case 161: The classes in package infrastructure.socialmedia.gowalla are not allowed to use modules in a higher layer
//Result: FALSE
public class VenueDAO {
	public VenueDAO(){
		try {
			getVenues();			
			//FR5.8
		} catch (GoWallaException e) {
			e.printStackTrace();
		}
	}

	//FR5.8
	public String getVenues() throws GoWallaException{
		//FR5.8
		throw new GoWallaException("No Wall available");
	}
}