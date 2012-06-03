package domain.locationbased.latitude;

import infrastructure.socialmedia.locationbased.latitude.LatitudeException;
//Functional requirement 3.2
//Test case 137: Class domain.locationbased.latitude.Statics is not allowed to use exception infrastructure.socialmedia.locationbased.foursquare.FourSquareException
//Result: TRUE
public class Statics {
	public Statics(){
		try {
			getStatics();			
			//FR5.8
		} catch (LatitudeException e) {
			e.printStackTrace();
		}
	}

	//FR5.8
	public String getStatics() throws LatitudeException{
		//FR5.8
		throw new LatitudeException("No statics available");
	}
}