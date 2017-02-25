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

	public CallInstanceSubClassDOA getProfileInformation(int i, string s){
		return subDao;
	}

	public POI getProfileInformation(string s, int i){
		return new POI();
	}

	public POI getMethodCorrectlyByDerivedArgumentType(string s, string j, int j){
		return new POI();
	}

	public CallInstanceSubClassDOA getMethodCorrectlyByDerivedArgumentType(string s, int i, int j){
		return subDao;
	}

	public CallInstanceSubClassDOA getMethodCorrectlyByHeuristic(int i, string s, int j){
		return subDao;
	}

	public POI getMethodCorrectlyByHeuristic(string s, string p, int i){
		return new POI();
	}

	}
}
