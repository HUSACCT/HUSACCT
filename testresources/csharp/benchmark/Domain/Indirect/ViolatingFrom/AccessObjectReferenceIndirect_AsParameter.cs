namespace Domain.Indirect.ViolatingFrom
{
	public class AccessObjectReferenceIndirect_AsParameter
	{
        private Domain.Indirect.Intermediate.BackgroundService bckg = new Domain.Indirect.Intermediate.BackgroundService();

        private Domain.Indirect.Intermediate.ServiceTwo st = new Domain.Indirect.Intermediate.ServiceTwo();

		public AccessObjectReferenceIndirect_AsParameter()
		{
			string s = bckg.GetServiceOneNameByArgument(st.GetServiceOne());            
		}
	}
}
