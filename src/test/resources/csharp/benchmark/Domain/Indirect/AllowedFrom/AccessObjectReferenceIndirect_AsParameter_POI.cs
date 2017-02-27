namespace Domain.Indirect.AllowedFrom
{
	public class AccessObjectReferenceIndirect_AsParameter_POI
	{
        private Domain.Indirect.Intermediate.BackgroundService bckg = new Domain.Indirect.Intermediate.BackgroundService();

        private Domain.Indirect.Intermediate.ServiceTwo st = new Domain.Indirect.Intermediate.ServiceTwo();

		public AccessObjectReferenceIndirect_AsParameter_POI()
		{
			string s = bckg.GetPOINameByArgument(st.GetServiceOne().poi);
		}
	}
}
