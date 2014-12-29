package domain.direct.violating;

import domain.direct.Base;

public class CallInstanceInnerInterface extends Base{

	public CallInstanceInnerInterface(){};
	
	public void test() {
		innerInterfaceDao.InterfaceMethod();
	}
	
}


