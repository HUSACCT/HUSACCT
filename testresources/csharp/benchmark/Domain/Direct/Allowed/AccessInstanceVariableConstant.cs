namespace Domain.Direct.Allowed
{
	using Domain.Direct;	

	public class AccessInstanceVariableConstant : Base
	{
		public string sniMessage;

		public void TestAccessFinalAttribute()
		{
			sniMessage = userDao.message;
		}
	}
}
