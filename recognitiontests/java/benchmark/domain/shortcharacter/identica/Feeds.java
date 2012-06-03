package domain.shortcharacter.identica;

import infrastructure.asocialmedia.ASocialNetwork;

//Functional requirement 3.1.1
//Test case 41: Class domain.shortcharacter.twitter.Followers may only have a dependency with class infrastructure.socialmedia.SocialNetworkInfo 
//Result: FALSE
public class Feeds {
	//FR5.5
	private ASocialNetwork asocialnetwork;

	//FR5.1
	public Feeds(){
		System.out.println(asocialnetwork.getASocialNetworkType());
	}
}