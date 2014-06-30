namespace Domain.Direct.Allowed
{
	public class CallInstanceSuperSuperClass : Base
	{
		public void MethodOfSuperClass()
		{
			subSubDao.MethodOnSuperClass();
		}
	}
}
