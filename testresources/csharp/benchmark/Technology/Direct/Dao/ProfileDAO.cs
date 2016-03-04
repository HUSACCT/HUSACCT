namespace Technology.Direct.Dao
{
	public class ProfileDAO
	{
		public CheckInDAO checkinValue;
		
		public string name = "profile";

		public virtual string GetCampaignType()
		{
			return "commercial";
		}
	}
}
