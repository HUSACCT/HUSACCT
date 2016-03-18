package domain.direct.violating;

import technology.direct.dao.AccountDAO;

public class CallConstructor {
	public CallConstructor(){

		new AccountDAO();
	}
}