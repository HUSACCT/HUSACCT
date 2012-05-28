package domain.locationbased.foursquare;

import infrastructure.socialmedia.locationbased.foursquare.FourSquareException;

//Functional requirement 3.2
//Test case 138: Class domain.locationbased.foursquare.Statics is not allowed to use exception infrastructure.socialmedia.locationbased.foursquare.FourSqaureException 
//Result: FALSE
public class Statics {
	public Statics(){
		try {
			getStatics();			
			//FR5.8
		} catch (FourSquareException e) {
			e.printStackTrace();
		}
	}

	//FR5.8
	public String getStatics() throws FourSquareException{
		//FR5.8
		throw new FourSquareException("No statics available");
	}
}