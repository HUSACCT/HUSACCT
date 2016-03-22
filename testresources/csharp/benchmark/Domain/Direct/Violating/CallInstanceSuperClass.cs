namespace Domain.Direct.Violating
{
	using Domain.Direct;
	using Technology.Direct.Subclass;

	public class CallInstanceSuperClass
	{
		CallInstanceSubClassDOA subDao;
		
		public void MethodOfSuperClass()
		{
			subDao.MethodOnSuperClass();
		}
	}
}
