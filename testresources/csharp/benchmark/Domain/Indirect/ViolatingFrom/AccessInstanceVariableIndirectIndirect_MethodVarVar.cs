namespace Domain.Indirect.ViolatingFrom
{
	public class AccessInstanceVariableIndirectIndirect_MethodVarVar
	{
        private Domain.Indirect.Intermediate.BackgroundService bckg;

        private string test;

        public void Case4()
        {
            bckg = new Domain.Indirect.Intermediate.BackgroundService();
            test = bckg.GetServiceTwo().serviceOne.name;
        }
	}
}
