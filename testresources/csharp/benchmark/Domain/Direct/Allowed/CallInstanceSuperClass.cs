namespace Domain.Direct.Violating
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
