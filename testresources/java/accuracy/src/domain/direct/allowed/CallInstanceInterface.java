package domain.direct.allowed;

import technology.direct.dao.CallInstanceInterfaceDAO;

public class CallInstanceInterface{
	
	private CallInstanceInterfaceDAO interfaceDao;

	public CallInstanceInterface(){};
	
	public void test() {
		interfaceDao.InterfaceMethod();
	}
	
}


