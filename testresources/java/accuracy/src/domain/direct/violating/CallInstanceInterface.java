package domain.direct.violating;

import technology.direct.dao.CallInstanceInterfaceDAO;

public class CallInstanceInterface{
	
	private CallInstanceInterfaceDAO interfaceDao;

	public CallInstanceInterface(){};
	
	public void test() {
		interfaceDao.InterfaceMethod();
	}
	
}


