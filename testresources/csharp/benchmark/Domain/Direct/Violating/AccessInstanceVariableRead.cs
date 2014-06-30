namespace Domain.Direct.Violating
{
	public class AccessInstanceVariable : Base
	{
		public AccessInstanceVariable()
		{
			string s = profileDao.name;
		}
	}
}
