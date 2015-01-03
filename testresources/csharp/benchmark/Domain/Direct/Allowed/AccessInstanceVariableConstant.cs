namespace Domain.Direct.Violating
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
