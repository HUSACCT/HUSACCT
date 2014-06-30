namespace Domain.Indirect.AllowedFrom
{
	public class AccessObjectReferenceIndirect_WithinIfStament_POI
	{
        private Domain.Indirect.Intermediate.ServiceTwo st = new Domain.Indirect.Intermediate.ServiceTwo();

		public AccessObjectReferenceIndirect_WithinIfStament_POI()
		{
			if (st.GetServiceOne().poi != null)
			{
				string s = "wrong";
			}
		}
	}
}
