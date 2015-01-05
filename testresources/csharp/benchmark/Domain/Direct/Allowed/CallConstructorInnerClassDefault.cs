using Technology.Direct.Dao;

namespace Domain.Direct.Allowed
{
	public class CallConstructorInnerClassDefault
	{
		public CallConstructorInnerClassDefault()
		{
            new CallInstanceOuterClassDAO.CallInstanceInnerClassDAO();
		}
	}
}

