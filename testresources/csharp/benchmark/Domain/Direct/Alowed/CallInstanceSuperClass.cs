namespace Domain.Direct.Allowed
{
	public class CallInstanceSuperClass : Base
	{
		public void MethodOfSuperClass()
		{
			subDao.MethodOnSuperClass();
		}
	}
}
