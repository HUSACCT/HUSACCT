namespace Domain.Direct.Violating
{
	public class CallConstructor
	{
		public CallConstructor()
		{
            new Technology.Direct.Dao.AccountDAO();
		}
	}
}
