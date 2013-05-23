namespace Domain.Direct.Allowed
{
	public class AccessInstanceVariableConstant : Base
	{
		public string sniMessage;

		public void TestAccessFinalAttribute()
		{
			sniMessage = userDao.message;
		}
	}
}
