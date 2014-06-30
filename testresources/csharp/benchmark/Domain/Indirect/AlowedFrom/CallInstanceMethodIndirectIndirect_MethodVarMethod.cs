namespace Domain.Indirect.AllowedFrom
{
	public class CallInstanceMethodIndirectIndirect_MethodVarMethod
	{
        private Domain.Indirect.Intermediate.BackgroundService bckg;

		private string test;

		public void Case6()
        {
            bckg = new Domain.Indirect.Intermediate.BackgroundService();
			test = (string)bckg.GetServiceTwo().serviceOne.GetName();
		}
	}
}
