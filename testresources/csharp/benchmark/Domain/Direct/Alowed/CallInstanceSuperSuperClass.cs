namespace Domain.Direct.Violating
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
