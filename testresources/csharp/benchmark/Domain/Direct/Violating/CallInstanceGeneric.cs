namespace Domain.Direct.Violating
{
	using Domain.Direct;	
	using Technology.Direct.Dao;

	public class CallInstanceGeneric
	{
		ProfileDAO<K, V> profileDao_Generic;

		public CallInstance()
		{
			profileDao_Generic.GetCampaignType();
		}
	}
}
