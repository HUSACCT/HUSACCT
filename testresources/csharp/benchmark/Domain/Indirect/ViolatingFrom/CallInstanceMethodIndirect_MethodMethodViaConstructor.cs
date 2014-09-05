namespace Domain.Indirect.ViolatingFrom
{
	public class CallInstanceMethodIndirect_MethodMethod_ViaConstructor
	{
		private string test;

		public void Case1()
		{
            test = new Domain.Indirect.Intermediate.BackgroundService().GetServiceOne().GetName();
		}
	}
}
