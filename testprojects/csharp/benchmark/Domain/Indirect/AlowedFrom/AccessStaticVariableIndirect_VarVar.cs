namespace Domain.Indirect.AllowedFrom
{
	public class AccessStaticVariableIndirect_VarVar
	{
        private Domain.Indirect.Intermediate.BackgroundService bckg;

        private string test;

        public void Case5()
        {
            bckg = new Domain.Indirect.Intermediate.BackgroundService();
            test = bckg.serviceOne.sName;
        }
	}
}
