package domain.direct.violating;

import technology.direct.dao.AccountDAO;
import technology.direct.dao.BadgesDAO;
import technology.direct.dao.CheckInDAO;
import technology.direct.dao.ProfileDAO;
import technology.direct.dao.UserDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

public class DeclarationVariableInstance_GenericType_OneTypeParameter {
	
	@SuppressWarnings("unused")
	private AccountDAO[] aDao;
	private List<BadgesDAO> bDao;
	private HashSet<CheckInDAO> cDao;
	private ArrayList<ProfileDAO> pDao;
	private Vector<UserDAO> uDao;
	private DeclarationParameter declaration;
 
	public String testUsageOfInstanceVariableDefinedByGenericTypeOneParameterType(){
		return pDao.get(0).toString();
	}
}