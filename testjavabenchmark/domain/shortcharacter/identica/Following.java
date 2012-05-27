package domain.shortcharacter.identica;

import infrastructure.asocialmedia.ASocialNetwork;

//Functional requirement 3.1.1
//Test case 45: Class domain.shortcharacter.identica.Following may only have a dependency with class infrastructure.socialmedia.SocialNetwork 
//Result: FALSE
public class Following {
	//FR5.5
	private ASocialNetwork asocialnetwork;
	public Following(){
		//FR5.2
		System.out.println(asocialnetwork.type);
	}
}