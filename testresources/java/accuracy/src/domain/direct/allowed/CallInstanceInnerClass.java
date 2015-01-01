package domain.direct.violating;

import domain.direct.Base;

public class CallInstanceInnerClass extends Base{
	
	public int CallMethodInstanceInnerClass() {
		int b;
		b = innerDao.getNext();
		return b;
	}
	
}
