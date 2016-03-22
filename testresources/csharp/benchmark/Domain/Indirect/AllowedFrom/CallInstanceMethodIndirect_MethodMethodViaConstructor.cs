namespace Domain.Indirect.AllowedFrom
{
	public class CallInstanceMethodIndirect_MethodMethodViaConstructor
	{
		private string test;

		public void Case1()
		{
            test = new Domain.Indirect.Intermediate.BackgroundService().GetServiceOne().GetName();
		}
	}
}
