package domain.direct;

import java.util.ArrayList;
import java.util.HashMap;

import technology.direct.dao.UserDAO;
import technology.direct.dao.ProfileDAO;
import technology.direct.dao.CallInstanceOuterClassDAO;
import technology.direct.dao.CallInstanceInterfaceDAO;
import fi.foyt.foursquare.api.FoursquareApi;
import technology.direct.subclass.CallInstanceSubClassDOA;
import technology.direct.subclass.CallInstanceSubSubClassDOA;

public class Base {
	
	protected UserDAO userDao;
	protected ProfileDAO profileDao;
	protected CallInstanceOuterClassDAO outerDao;
	protected CallInstanceOuterClassDAO.CallInstanceInnerClassDAO innerAttribute; // Declaration of inner class, while only the outer class is imported
	protected CallInstanceOuterClassDAO.CallInstanceInnerInterfaceDAO innerInterfaceDao;
	protected CallInstanceInterfaceDAO interfaceDao;
	protected FoursquareApi fourApi;
	protected CallInstanceSubClassDOA subDao;
	protected CallInstanceSubSubClassDOA subSubDao;
	protected ArrayList<ProfileDAO> profileDAOs;
	protected HashMap<ProfileDAO, UserDAO> hashMap;

	public Base(){
	}
		
	public ProfileDAO getProfileInformation(String s, int i){
		return new ProfileDAO();
	}

}