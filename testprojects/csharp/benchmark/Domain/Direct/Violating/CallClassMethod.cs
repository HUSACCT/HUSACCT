namespace Domain.Direct.Violating
{
	public class CallClassMethod
	{
		public CallClassMethod()
		{
			Technology.Direct.Dao.BadgesDAO.GetAllBadges();
		}
	}
}
