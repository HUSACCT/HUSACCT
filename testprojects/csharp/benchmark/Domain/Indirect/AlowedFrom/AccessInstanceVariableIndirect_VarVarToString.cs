namespace Domain.Indirect.AllowedFrom
{
	public class AccessInstanceVariableIndirect_VarVarToString
	{
        private Domain.Indirect.Intermediate.BackgroundService bckg;

        private string test;

        public void Case3()
        {
            bckg = new Domain.Indirect.Intermediate.BackgroundService();
            test = bckg.serviceOne.day.ToString();
        }
	}
}
