namespace Domain.Indirect.IndirectTo
{
	public class ServiceOne
	{
		public string sName = "ServiceOne";

		public string name;

        public System.DateTime day;

		public POI poi;

		public ServiceOne()
		{
			name = "ServiceOne";
            day = System.DateTime.Now;
		}

		public string GetName()
		{
			return name;
		}

		public static string GetsName()
		{
			return new ServiceOne().sName;
		}

        public System.DateTime GetDay()
		{
			return day;
		}

		public POI GetPOI()
		{
			return poi;
		}
	}
}
