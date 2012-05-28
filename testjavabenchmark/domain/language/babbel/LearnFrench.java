package domain.language.babbel;

import infrastructure.asocialmedia.ASocialNetwork;


//Functional requirement 3.1.3
//Test case 99: Class domain.language.babbel.LearnFrench must use class infrastructure.socialmedia.SocialNetwork 
//Result: FALSE
public class LearnFrench {
	//FR5.5
	private ASocialNetwork asocialnetwork;
	public LearnFrench(){
		//FR5.2
		//System.out.println(asocialnetwork.type);
		asocialnetwork.type = "klant";
	}
}