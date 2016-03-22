namespace Domain.Direct.Violating
{
	using Domain.Direct;	
	using Technology.Direct.Dao;

	public class AccessInstanceVariableWrite
	{
		private ProfileDAO profileDao;

		public AccessInstanceVariable()
		{
			String s = "profit";
			profileDao.name = s;
		}
	}
}
