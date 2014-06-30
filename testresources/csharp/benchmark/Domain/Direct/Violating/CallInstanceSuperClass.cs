namespace Domain.Direct.Violating
{
	public class CallInstanceSuperClass : Base
	{
		public void MethodOfSuperClass()
		{
			subDao.MethodOnSuperClass();
		}
	}
}
