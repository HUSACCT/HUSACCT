package domain.shortcharacter.twitter;

import infrastructure.socialmedia.SocialNetworkInfo;

//Functional requirement 3.1.1
//Test case 38: Class domain.shortcharacter.twitter.AdvertiseTweet may only have a dependency with class have a dependency with class infrastructure.socialmedia.SocialNetworkInfo 
//Result: TRUE
public class AdvertiseTweet {
	public AdvertiseTweet(){
		//FR5.1
		System.out.println(SocialNetworkInfo.getInfo());
	}
}