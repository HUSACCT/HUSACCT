package domain.language.busuu;

import infrastructure.socialmedia.SocialNetwork;

//Functional requirement 3.1.3
//Test case 98: Class domain.language.busuu.LearnFrench must use class infrastructure.socialmedia.SocialNetwork 
//Result: TRUE
public class LearnFrench {
	//FR5.5
	private SocialNetwork socialnetwork;
	
	public LearnFrench(){
		//FR5.2
		//System.out.println(socialnetwork.type);
		socialnetwork.type = "klant";
	}
}