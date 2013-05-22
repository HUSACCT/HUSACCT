namespace Domain.Direct.Violating
{
	public class DeclarationTypeCastOfArgument
	{
		public void InitializeProfileInformation()
		{
            GetProfileInformation((Technology.Direct.Dao.ProfileDAO)new object());
		}

		public string GetProfileInformation(object o)
		{
			return o.ToString();
		}
	}
}
