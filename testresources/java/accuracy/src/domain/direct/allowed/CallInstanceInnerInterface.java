package domain.direct.allowed;

import domain.direct.Base;

public class CallInstanceInnerInterface extends Base{

	public CallInstanceInnerInterface(){};
	
	public void test() {
		innerInterfaceDao.InterfaceMethod();
	}
	
}


