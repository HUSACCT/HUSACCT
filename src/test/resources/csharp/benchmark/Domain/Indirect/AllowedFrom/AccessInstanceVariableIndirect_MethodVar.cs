namespace Domain.Indirect.AllowedFrom
{
	public class AccessInstanceVariableIndirect_MethodVar
	{
		private Domain.Indirect.Intermediate.BackgroundService bckg;

		private string test;
 
		public void Case1()
		{
			bckg = new Domain.Indirect.Intermediate.BackgroundService();
			test = bckg.GetServiceOne().name;
		}
	}
}
