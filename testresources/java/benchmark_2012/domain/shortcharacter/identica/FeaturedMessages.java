package domain.shortcharacter.identica;

import infrastructure.asocialmedia.ASocialNetworkInfo;

//Functional requirement 3.1.1
//Test case 39: Class domain.shortcharacter.identica.FeaturedMessages may only have a dependency with class infrastructure.socialmedia.SocialNetworkInfo
//Result: FALSE
public class FeaturedMessages {
	public FeaturedMessages(){
		//FR5.1
		System.out.println(ASocialNetworkInfo.getInfo());
	}
}