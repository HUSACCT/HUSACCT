namespace Domain.Direct.Violating
{
	using Domain.Direct;

	public class CallInstanceInterface : Base
	{
		public void Test()
		{
			interfaceDao.InterfaceMethod();
		}
	}
}
