package domain.language.babbel;

import infrastructure.asocialmedia.ASocialNetworkInfo;

//Functional requirement 3.1.3
//Test case 93: Class domain.language.babbel.LearnChinese must use class infrastructure.socialmedia.SocialNetworkInfo 
//Result: FALSE
public class LearnChinese {
	public LearnChinese(){
		//FR5.1
		System.out.println(ASocialNetworkInfo.getInfo());
	}
}