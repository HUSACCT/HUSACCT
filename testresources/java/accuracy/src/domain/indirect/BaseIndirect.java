package domain.indirect;

import technology.direct.subclass.CallInstanceSubClassDOA;
import technology.direct.subclass.CallInstanceSubSubClassDOA;
import domain.indirect.indirectTo.POI;

public class BaseIndirect {
	
	protected CallInstanceSubClassDOA subDao;
	protected CallInstanceSubSubClassDOA subSubDao;
	

	public BaseIndirect(){
		
	}

	public POI getProfileInformation(String s, int i){
		return new POI();
	}

}