package presentation.gui.stumbleupon;

import domain.stumbleupon.Account;
//Functional requirement 3.2.2
//Test case 165: All classes in package presentation.gui.stumbleupon are not allowed to use in a not direct lower layer
//Result: TRUE
public class AccountGUI {
	public AccountGUI(){
		//FR5.1
		new Account();
	}
}