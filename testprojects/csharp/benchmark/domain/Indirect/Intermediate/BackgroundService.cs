namespace Domain.Indirect.Intermediate
{
	public class BackgroundService
	{
        public static Domain.Indirect.IndirectTo.ServiceOne serviceOneStaticAttribute;

        public Domain.Indirect.IndirectTo.ServiceOne serviceOne;

		public ServiceTwo serviceTwo;

        public static Domain.Indirect.IndirectTo.ServiceOne GetServiceOneviaStaticAttribute()
		{
			return serviceOneStaticAttribute;
		}

        public virtual Domain.Indirect.IndirectTo.ServiceOne GetServiceOne()
		{
			return serviceOne;
		}

		public virtual ServiceTwo GetServiceTwo()
		{
			return serviceTwo;
		}

        public virtual string GetServiceOneNameByArgument(Domain.Indirect.IndirectTo.ServiceOne so)
		{
			return so.GetName();
		}

        public virtual string GetPOINameByArgument(Domain.Indirect.IndirectTo.POI poi)
		{
			return poi.GetName();
		}
	}
}
