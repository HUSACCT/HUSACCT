package domain.direct.allowed;

import technology.direct.dao.HistoryDAO;

public class CallInstanceOfSuperOverridden extends HistoryDAO {
	
	public void printMethod() {
		super.printMethod();
        System.out.println("Printed in Subclass.");
    }

}
