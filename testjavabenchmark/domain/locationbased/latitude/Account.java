package domain.locationbased.latitude;

import infrastructure.socialmedia.locationbased.latitude.AccountDAO;

//Functional requirement 3.2
//Test case 115: Class domain.locationbased.latitude.Account is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.AccountDAO 
//Result: TRUE
public class Account {
	public Account(){
		//FR5.1
		new AccountDAO();
	}
}