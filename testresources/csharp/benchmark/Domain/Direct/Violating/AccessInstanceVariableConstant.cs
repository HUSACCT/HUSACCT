
namespace Domain.Direct.Violating
{
	using Domain.Direct;	
	using Technology.Direct.Dao;

	public class AccessInstanceVariableConstant
	{
		public UserDAO userDao;
		public string sniMessage;

		public void TestAccessFinalAttribute()
		{
			sniMessage = userDao.message;
		}
	}
}
