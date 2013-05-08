namespace Domain.Indirect.AllowedFrom
{
	public class CallStaticMethodIndirect_VarStaticMethod
	{
		public void Case5()
		{
            string s = Domain.Indirect.IndirectTo.ServiceOne.GetsName();
		}
	}
}
