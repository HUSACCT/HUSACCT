namespace Domain.Indirect.ViolatingFrom
{
	public class CallInstanceMethodIndirect_MethodMethodToString
	{
        private Domain.Indirect.Intermediate.BackgroundService bckg;

		private string test;

		public void Case4()
        {
            bckg = new Domain.Indirect.Intermediate.BackgroundService();
			test = bckg.GetServiceOne().GetDay().ToString();
		}
	}
}
