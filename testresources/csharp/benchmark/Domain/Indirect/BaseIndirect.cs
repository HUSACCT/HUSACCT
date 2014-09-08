using Technology.Direct.Subclass;
using Domain.Indirect.IndirectTo;

namespace Domain.Indirect
{
	public class BaseIndirect
	{

        		protected CallInstanceSubClassDOA subDao;

        		protected CallInstanceSubSubClassDOA subSubDao;

		public BaseIndirect()
		{
		}

	public subDao getProfileInformation(int i, string s){
		return subDao;
	}

	public POI getProfileInformation(string s, int i){
		return new POI();
	}

	public subDao getMethodCorrectlyByDerivedArgumentType(string s, string j, int j){
		return subDao;
	}

	public POI getMethodCorrectlyByDerivedArgumentType(string s, int i, int j){
		return new POI();
	}

	public subDao getMethodCorrectlyByHeuristic(int i, string s, int j){
		return subDao;
	}

	public POI getMethodCorrectlyByHeuristic(string s, string p, int i){
		return new POI();
	}

	}
}
