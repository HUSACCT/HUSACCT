namespace Domain.Direct.Violating
{
	public class CallInstanceSuperSuperClass : Base
	{
		public void MethodOfSuperClass()
		{
			subSubDao.MethodOnSuperClass();
		}
	}
}
