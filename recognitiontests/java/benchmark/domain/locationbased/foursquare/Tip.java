package domain.locationbased.foursquare;

import infrastructure.socialmedia.locationbased.foursquare.TipDAO;
//Functional requirement 3.2
//Test case 140: Class domain.locationbased.foursquare.Tip is not allowed to use enumeration infrastructure.socialmedia.locationbased.foursquare.TipDAO
//Result: FALSE
public class Tip {
	public Tip(){
		//FR5.2
		System.out.println(TipDAO.ONE);
	}
}