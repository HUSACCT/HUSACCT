namespace Domain.Indirect.ViolatingFrom
{
	public class AccessObjectReferenceIndirect_WithinIfStament
	{
        private Domain.Indirect.Intermediate.ServiceTwo st = new Domain.Indirect.Intermediate.ServiceTwo();

		public AccessObjectReferenceIndirect_WithinIfStament()
		{
			if (st.GetServiceOne() != null)
			{
				string s = "wrong";
			}
		}
	}
}
