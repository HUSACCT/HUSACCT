namespace Domain.Indirect.Intermediate
{
	public class ServiceTwo
	{
		public static string sName = "ServiceTwo";

		public string name;

		public System.DateTime day;

        public Domain.Indirect.IndirectTo.ServiceOne serviceOne;

		public ServiceTwo()
		{
			name = "ServiceTwo";
            day = System.DateTime.Now;
		}

		public virtual string GetName()
		{
			return name;
		}

        public virtual System.DateTime GetDay()
		{
			return day;
		}

		public static string GetsName()
		{
			return sName;
		}

        public Domain.Indirect.IndirectTo.ServiceOne GetServiceOne()
		{
			return serviceOne;
		}
	}
}
