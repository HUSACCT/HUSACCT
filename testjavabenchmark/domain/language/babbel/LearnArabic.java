package domain.language.babbel;

import infrastructure.asocialmedia.ASocialNetwork;

//Functional requirement 3.1.3
//Test case 91: Class domain.language.babbel.LearnArabic must use infrastructure.socialmedia.SocialNetwork 
//Result: FALSE
public class LearnArabic {
	public LearnArabic(){
		//FR5.1
		new ASocialNetwork();
	}
}