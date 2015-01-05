namespace Domain.Direct.Allowed
{
	using Domain.Direct;

	public class CallInstanceInnerInterface : Base
	{
		public CallInstanceInnerInterface(){}
		
		public void Test()
		{
			innerInterfaceDao.InterfaceMethod();
		}
	}
}

