namespace Domain.Direct.Violating
{
	using Technology.Direct.Dao;	

	public class AccessLocalVariable_SetArgumentValue
	{

	public string theMethod(string s, int i) {
		ProfileDAO profile;
		string p1 = getProfileInformation(profile, "test", 4).checkinValue;
		return p1;
	}
	
	public string getProfileInformation(string s, ProfileDAO profile, int i) {
		string p2 = s;
		return p2;
	}

	public ProfileDAO getProfileInformation(ProfileDAO profile, string s, int i) {
		string p3 = s;
		return p3;
	}
	}
}
