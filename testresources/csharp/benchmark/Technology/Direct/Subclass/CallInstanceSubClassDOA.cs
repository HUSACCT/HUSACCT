using Technology.Direct.Dao;

namespace Technology.Direct.Subclass
{
	public class CallInstanceSubClassDOA : CallInstanceSuperClassDAO
	{
		public CallInstanceSubClassDOA()
		{
		}

		public CallInstanceSuperClassDAO MethodSuperClassInstance;

		public virtual void Test()
		{
			MethodSuperClassInstance.MethodOnSuperClass();
		}
	}
}
