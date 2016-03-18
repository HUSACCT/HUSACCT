using Domain.Indirect;

namespace Domain.Indirect.ViolatingFrom
{

	public class AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaArgumentType

	{
	public string initializeProfileInformation() {
		string profile = "profiled";
		BaseIndirect baseClass = new BaseIndirect();
		Object o = baseClass.getMethodCorrectlyByDerivedArgumentType("tsja", profile, 3);
		return o;	}

	}
}
