namespace Domain.Direct.Violating
{
	public class DeclarationTypeCast
	{
		public void GetProfileInformation()
		{
            object o = (Technology.Direct.Dao.ProfileDAO)new object();
		}
	}
}
