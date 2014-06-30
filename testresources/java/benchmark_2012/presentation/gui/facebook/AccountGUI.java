package presentation.gui.facebook;

import domain.facebook.Account;

//Functional requirement 3.1.2
//Test case 66: Only class presentation.gui.facebook.AccountGUI may have a dependency with class domain.Facebook.Account
//Result: FALSE
public class AccountGUI {
	public AccountGUI(){
		//FR5.1
		new Account();
	}
}