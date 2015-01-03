namespace Domain.Direct.Violating
{
	using Technology.Direct.Dao;	

	public class AccessLocalVariable_ReadArgumentValue
	{
	public string getProfileInformation(string s, ProfileDAO profile, int i) {
		string pn = profile.name;
		return pn;
	}

	}
}
