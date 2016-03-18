package domain.direct.violating;

import technology.direct.dao.CallInstanceOuterClassDAO;

public class CallInstanceInnerClass {
	
	protected CallInstanceOuterClassDAO.CallInstanceInnerClassDAO innerDao; // Declaration of inner class, while only the outer class is imported

	public int CallMethodInstanceInnerClass() {
		int b;
		b = innerDao.getNext();
		return b;
	}
	
}
