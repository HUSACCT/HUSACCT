namespace Domain.Indirect.ViolatingFrom
{
	public class CallInstanceMethodIndirect_StaticMethodInstanceMethod
	{
		
		public void Case5()
		{
            string s = Domain.Indirect.Intermediate.BackgroundService.GetServiceOneviaStaticAttribute().GetName();
		}
	}
}
