namespace Domain.Direct.Violating
{
	using Domain.Direct;	

	public class CallInstance : Base
	{
		public CallInstance()
		{
			profileDao.GetCampaignType();
		}
	}
}
