using Technology.Direct.Dao;

namespace Domain.Direct.Violating
{
	public class CallConstructorInnerClass
	{
		public CallConstructorInnerClass()
		{
            new CallInstanceOuterClassDAO.CallInstanceInnerClassDAO("text");
		}
	}
}

