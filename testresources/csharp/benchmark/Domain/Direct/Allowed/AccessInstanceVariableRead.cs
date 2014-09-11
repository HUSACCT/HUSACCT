namespace Domain.Direct.Allowed
{
	using Domain.Direct;	

	public class AccessInstanceVariableRead : Base
	{
		public AccessInstanceVariable()
		{
			string s = profileDao.name;
		}
	}
}
