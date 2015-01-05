using Technology.Direct.Dao;

namespace Domain.Direct.Allowed
{
	public class CallConstructorInnerClass
	{
		public CallConstructorInnerClass()
		{
            new CallInstanceOuterClassDAO.CallInstanceInnerClassDAO("text");
		}
	}
}

