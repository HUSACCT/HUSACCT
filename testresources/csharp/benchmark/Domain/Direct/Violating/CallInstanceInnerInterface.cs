namespace Domain.Direct.Violating
{
	using Domain.Direct;
	using Technology.Direct.Dao;

	public class CallInstanceInnerInterface
	{
		CallInstanceOuterClassDAO.CallInstanceInnerInterfaceDAO innerInterfaceDao;

		public CallInstanceInnerInterface(){}
		
		public void Test()
		{
			innerInterfaceDao.InterfaceMethod();
		}
	}
}

