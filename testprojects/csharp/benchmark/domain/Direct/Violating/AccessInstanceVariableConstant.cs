namespace Domain.Direct.Violating
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
