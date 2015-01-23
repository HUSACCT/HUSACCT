namespace Domain.Direct.Violating
{
	using Domain.Direct;

	public class CallInstanceInterfaceGenericInterface: Base
	{
		public void Test()
		{
			interfaceDao_Generic.InterfaceMethod();
		}
	}
}
