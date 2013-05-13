namespace Domain.Indirect.ViolatingFrom
{
	public class CallStaticMethodIndirect_MethodStaticMethod
	{
        private Domain.Indirect.Intermediate.BackgroundService bckg;

		public void Case5()
        {
            bckg = new Domain.Indirect.Intermediate.BackgroundService();
            string s = Domain.Indirect.IndirectTo.ServiceOne.GetsName();
		}
	}
}
