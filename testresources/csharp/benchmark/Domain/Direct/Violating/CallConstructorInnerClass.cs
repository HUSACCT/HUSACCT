using Technology.Direct.Dao;

namespace Domain.Direct.Violating
{
	public class CallConstructor
	{
		public CallConstructorInnerClass()
		{
            new CallInstanceOuterClassDAO.CallInstanceInnerClassDAO();
		}
	}
}

