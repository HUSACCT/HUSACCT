package domain.shortcharacter.identica;

import infrastructure.asocialmedia.ASocialNetworkInfo;

//Functional requirement 3.1.1
//Test case 43: Class domain.shortcharacter.identica.Followers may only have a dependency with class infrastructure.socialmedia.SocialNetworkInfo
//Result: FALSE
public class Followers {
	public Followers(){
		//FR5.2
		System.out.println(ASocialNetworkInfo.message);
	}
}