package domain.language.babbel;

import infrastructure.asocialmedia.ASocialNetwork;

//Functional requirement 3.1.3
//Test case 95: Class domain.language.babbel.LearnDutch must use class infrastructure.socialmedia.SocialNetwork 
//Result: FALSE
public class LearnDutch {
	//FR5.5
	private ASocialNetwork asocialnetwork;

	//FR5.1
	public LearnDutch(){
		System.out.println(asocialnetwork.getASocialNetworkType());
	}
}