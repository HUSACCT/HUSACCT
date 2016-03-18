namespace Domain.Direct.Violating
{
	using Domain.Direct;	
	using Technology.Direct.Dao;

	public class CallInstance
	{
		private ProfileDAO profileDao;

		public CallInstance()
		{
			profileDao.GetCampaignType();
		}
	}
}
