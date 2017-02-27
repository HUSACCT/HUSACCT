using Domain.Indirect;
using Domain.Indirect.Intermediate;

namespace Domain.Indirect.AllowedFrom
{

	public class AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaHeuristic
	{
		ServiceTwo st = new ServiceTwo();

	public string initializeProfileInformation() {
		BaseIndirect baseClass = new BaseIndirect();
		Object o = baseClass.getMethodCorrectlyByHeuristic("tsja", st.getServiceOne(), 3);
		return o;	
	}

	}
}
