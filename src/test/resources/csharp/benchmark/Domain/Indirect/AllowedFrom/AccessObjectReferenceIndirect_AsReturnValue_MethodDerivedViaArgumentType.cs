using Domain.Indirect;

namespace Domain.Indirect.AllowedFrom
{

	public class AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaArgumentType

	{
	public string initializeProfileInformation() {
		String profile = "profiled";
		BaseIndirect baseClass = new BaseIndirect();
		Object o = baseClass.getMethodCorrectlyByDerivedArgumentType("tsja", profile, 3);
		return o;	}

	}
}
