using System;
using System.Collections.Generic;

namespace Technology.Direct.Dao
{
	public class ProfileDAO<K, V>
	{
		public CheckInDAO checkinValue;
		
		public string name = "profile";

		public virtual string GetCampaignType()
		{
			return "commercial";
		}
	}
}
