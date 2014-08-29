package domain.indirect;

import technology.direct.subclass.CallInstanceSubClassDOA;
import technology.direct.subclass.CallInstanceSubSubClassDOA;
import domain.indirect.indirectto.POI;

public class BaseIndirect {
	
	protected CallInstanceSubClassDOA subDao;
	protected CallInstanceSubSubClassDOA subSubDao;
	

	public BaseIndirect(){
		
	}

	public subDao getProfileInformation(int i, String s){
		return subDao;
	}

	public POI getProfileInformation(String s, int i){
		return new POI();
	}

	public subDao getMethodCorrectlyByDerivedArgumentType(String s, String j, int j){
		return subDao;
	}

	public POI getMethodCorrectlyByDerivedArgumentType(String s, int i, int j){
		return new POI();
	}

	public subDao getMethodCorrectlyByHeuristic(int i, String s, int j){
		return subDao;
	}

	public POI getMethodCorrectlyByHeuristic(String s, String p, int i){
		return new POI();
	}

}