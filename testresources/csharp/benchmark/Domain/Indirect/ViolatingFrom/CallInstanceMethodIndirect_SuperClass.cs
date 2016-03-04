using Domain.Indirect;

namespace Domain.Indirect.ViolatingFrom
{
	public class CallInstanceMethodIndirect_SuperClass : BaseIndirect
	{
		public void MethodOfSuperClass()
		{
			subDao.MethodOnSuperClass();
		}
	}
}
