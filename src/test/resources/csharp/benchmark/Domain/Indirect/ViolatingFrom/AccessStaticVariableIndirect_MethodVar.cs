namespace Domain.Indirect.ViolatingFrom
{
	public class AccessStaticVariableIndirect_MethodVar
	{
        private Domain.Indirect.Intermediate.BackgroundService bckg;

        private string test;

        public void Case5()
        {
            bckg = new Domain.Indirect.Intermediate.BackgroundService();
            test = bckg.GetServiceOne().sName;
        }
	}
}
