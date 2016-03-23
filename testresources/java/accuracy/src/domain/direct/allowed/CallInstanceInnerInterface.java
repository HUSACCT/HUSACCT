package domain.direct.allowed;

import technology.direct.dao.CallInstanceOuterClassDAO;

public class CallInstanceInnerInterface {

	protected CallInstanceOuterClassDAO.CallInstanceInnerInterfaceDAO innerInterfaceDao;

	public CallInstanceInnerInterface(){};
	
	public void test() {
		innerInterfaceDao.InterfaceMethod();
	}
	
}


