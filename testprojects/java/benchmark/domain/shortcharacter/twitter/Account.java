package domain.shortcharacter.twitter;

import infrastructure.socialmedia.SocialNetwork;
//Functional requirement 3.1.1
//Test case 36: Class domain.shortcharacter.twitter.Account may only have a dependency with class infrastructure.socialmedia.SocialNetwork
//Result: TRUE
public class Account {	
	public Account(){
		//FR5.1
		new SocialNetwork();
	}
}