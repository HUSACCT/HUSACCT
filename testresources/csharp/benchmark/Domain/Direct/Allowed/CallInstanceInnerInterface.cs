namespace Domain.Direct.Violating
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

