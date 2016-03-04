namespace Domain.Direct.Violating
{
	using Domain.Direct;	
	using Technology.Direct.Dao;

	public class AccessInstanceVariableRead
	{
		private ProfileDAO profileDao;

		public AccessInstanceVariable()
		{
			string s = profileDao.name;
		}
	}
}
