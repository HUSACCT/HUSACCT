package domain.shortcharacter.identica;

import infrastructure.asocialmedia.ASocialNetworkInfo;

//Functional requirement 3.1.1
//Test case 53: Class domain.shortcharacter.identica.Message may only have a dependency with class infrastructure.asocialmedia.ASocialNetwork
//Result: FALSE

public class Message{
	//FR5.5
	public String getNetworkInformation(ASocialNetworkInfo info){
		return "";
	}
}