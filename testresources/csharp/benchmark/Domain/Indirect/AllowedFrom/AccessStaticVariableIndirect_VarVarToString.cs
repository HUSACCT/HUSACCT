namespace Domain.Indirect.AllowedFrom
{
	public class AccessStaticVariableIndirect_VarVarToString
	{
        private Domain.Indirect.Intermediate.BackgroundService bckg;

		private string test;

		public void Case7()
		{
			test = bckg.serviceOne.sName.ToString();
		}
	}
}
