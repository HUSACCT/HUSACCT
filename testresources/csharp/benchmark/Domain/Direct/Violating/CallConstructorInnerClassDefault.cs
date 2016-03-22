using Technology.Direct.Dao;

namespace Domain.Direct.Violating
{
	public class CallConstructorInnerClassDefault
	{
		public CallConstructorInnerClassDefault()
		{
            new CallInstanceOuterClassDAO.CallInstanceInnerClassDAO();
		}
	}
}

