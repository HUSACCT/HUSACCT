namespace Domain.Direct.Violating
{
	using Domain.Direct;
	using Technology.Direct.Subclass;

	public class CallInstanceSuperSuperClass
	{
		CallInstanceSubSubClassDOA subSubDao;

		public void MethodOfSuperClass()
		{
			subSubDao.MethodOnSuperClass();
		}
	}
}
