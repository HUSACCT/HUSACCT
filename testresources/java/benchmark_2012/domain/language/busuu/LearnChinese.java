package domain.language.busuu;

import infrastructure.socialmedia.SocialNetworkInfo;

//Functional requirement 3.1.3
//Test case 92: Class domain.language.busuu.LearnChinese must use class infrastructure.socialmedia.SocialNetworkInfo 
//Result: TRUE
public class LearnChinese {
	public LearnChinese(){
		//FR5.1
		System.out.println(SocialNetworkInfo.getInfo());
	}
}