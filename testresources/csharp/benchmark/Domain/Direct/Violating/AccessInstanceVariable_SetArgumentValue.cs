namespace Domain.Direct.Violating
{
	using Technology.Direct.Dao;	

	public class AccessInstanceVariable_SetArgumentValue
	{
		ProfileDAO profile;

	public string theMethod(string s, int i) {
		string pn = getProfileInformation(profile, "test", 4).checkinValue;
		return pn;
	}
	
	public string getProfileInformation(string s, ProfileDAO profile, int i) {
		string pn = s;
		return pn;
	}

	public ProfileDAO getProfileInformation(ProfileDAO profile, string s, int i) {
		string pn = s;
		return pn;
	}
	}
}
