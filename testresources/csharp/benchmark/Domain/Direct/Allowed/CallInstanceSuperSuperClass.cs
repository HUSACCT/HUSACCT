namespace Domain.Direct.Allowed
{
	using Domain.Direct;

	public class CallInstanceSuperSuperClass : Base
	{
		public void MethodOfSuperClass()
		{
			subSubDao.MethodOnSuperClass();
		}
	}
}
