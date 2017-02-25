using Technology.Direct.Dao;

namespace Technology.Direct.Subclass
{
	public class CallInstanceSubSubClassDOA : CallInstanceSubClassDOA
	{
		public CallInstanceSuperClassDAO MethodSuperClassInstance;

		public CallInstanceSubSubClassDOA()
		{
		}

		public override void Test()
		{
			MethodSuperClassInstance.MethodOnSuperClass();
		}
	}
}
