namespace Domain.Indirect.ViolatingFrom
{
	public class CallInstanceMethodIndirectIndirect_VarVarMethod
	{
		private Domain.Indirect.Intermediate.BackgroundService bckg;

		private string test;

        public void Case6()
        {
            bckg = new Domain.Indirect.Intermediate.BackgroundService();
			bckg.serviceTwo.serviceOne.GetName();
		}
	}
}
