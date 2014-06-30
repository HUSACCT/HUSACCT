package domain.shortcharacter.twitter;

import infrastructure.socialmedia.SocialNetworkInfo;

//Functional requirement 3.1.1
//Test case 42: Class domain.shortcharacter.twitter.Following may only have a dependency with class infrastructure.socialmedia.SocialNetworkInfo
//Result: TRUE
public class Following {
	public Following(){
		//FR5.2
		System.out.println(SocialNetworkInfo.message);
	}
}