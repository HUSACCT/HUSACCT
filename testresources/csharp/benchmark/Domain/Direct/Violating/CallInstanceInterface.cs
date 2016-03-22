namespace Domain.Direct.Violating
{
	using Domain.Direct;
	using Technology.Direct.Dao;

	public class CallInstanceInterface
	{
		CallInstanceInterfaceDAO interfaceDao;

		public void Test()
		{
			interfaceDao.InterfaceMethod();
		}
	}
}
