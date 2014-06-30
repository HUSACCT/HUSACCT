namespace Domain.Indirect.AllowedFrom
{
	public class CallInstanceMethodIndirect_VarMethod
	{
        private Domain.Indirect.Intermediate.BackgroundService bckg;

		private string test;
 
		public void Case2()
		{
            bckg = new Domain.Indirect.Intermediate.BackgroundService();
			test = bckg.serviceOne.GetName();
		}
	}
}
