namespace Domain.Direct.Violating
{
	using Technology.Direct.Dao;

	public class CallInstanceInterfaceGenericInterface
	{
		private CallInstanceInterfaceDAO<K> interfaceDao_Generic;

		public void Test()
		{
			interfaceDao_Generic.InterfaceMethod();
		}
	}
}
