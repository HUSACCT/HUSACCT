namespace Domain.Indirect.ViolatingFrom
{
	public class AccessInstanceVariableIndirect_VarVar
	{
        private Domain.Indirect.Intermediate.BackgroundService bckg;

		private string test;
 
		public void Case1()
		{
            bckg = new Domain.Indirect.Intermediate.BackgroundService();
			test = bckg.serviceOne.name;
		}
	}
}
