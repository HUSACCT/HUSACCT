namespace Domain.Direct.Violating
{
	using Domain.Direct;	

	public class AccessInstanceVariable : Base
	{
		public AccessInstanceVariable()
		{
			string s = profileDao.name;
		}
	}
}
