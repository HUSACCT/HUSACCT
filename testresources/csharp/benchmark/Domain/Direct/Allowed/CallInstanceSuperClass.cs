namespace Domain.Direct.Allowed
{
	using Domain.Direct;

	public class CallInstanceSuperClass : Base
	{
		public void MethodOfSuperClass()
		{
			subDao.MethodOnSuperClass();
		}
	}
}
