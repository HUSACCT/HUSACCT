namespace Domain.Direct.Allowed
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
