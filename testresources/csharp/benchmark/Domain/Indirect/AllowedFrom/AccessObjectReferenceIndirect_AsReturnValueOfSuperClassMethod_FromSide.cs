using Domain.Indirect;

namespace Domain.Indirect.AllowedFrom
{

	public class AccessObjectReferenceIndirect_AsReturnValueOfSuperClassMethod_FromSide : BaseIndirect

	{
	public string initializeProfileInformation() {
		String profile = "profiled";
		Object o = getProfileInformation("tsja", 3);
		return o;
	}

	}
}
