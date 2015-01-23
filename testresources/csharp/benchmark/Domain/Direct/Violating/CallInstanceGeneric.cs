namespace Domain.Direct.Violating
{
	using Domain.Direct;	

	public class CallInstanceGeneric : Base
	{
		public CallInstance()
		{
			profileDao_Generic.GetCampaignType();
		}
	}
}
