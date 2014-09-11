namespace Domain.Indirect.AllowedFrom
{
	public class AccessStaticVariableIndirectIndirect_MethodVarVar
	{
        private Domain.Indirect.Intermediate.BackgroundService bckg;

		private string test;

		public void Case8()
        {
            bckg = new Domain.Indirect.Intermediate.BackgroundService();
			test = bckg.GetServiceTwo().serviceOne.sName;
		}
	}
}
