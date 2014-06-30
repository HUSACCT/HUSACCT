package domain.shortcharacter.identica;

import infrastructure.asocialmedia.ASocialNetwork;
//Functional requirement 3.1.1
//Test case 37: Class domain.shortcharacter.identica.Account may only have a dependency with class infrastructure.asocialmedia.ASocialNetwork
//Result: FALSE
public class Account{
	public Account(){
		//FR5.1
		new ASocialNetwork();
	}
}