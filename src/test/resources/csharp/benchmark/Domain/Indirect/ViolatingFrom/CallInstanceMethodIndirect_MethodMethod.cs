namespace Domain.Indirect.ViolatingFrom
{
	public class CallInstanceMethodIndirect_MethodMethod
	{
        private Domain.Indirect.Intermediate.BackgroundService bckg;

		private string test;

		public void Case2()
        {
            bckg = new Domain.Indirect.Intermediate.BackgroundService();
			test = bckg.GetServiceOne().GetName();
		}
	}
}
