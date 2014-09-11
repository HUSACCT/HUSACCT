using Domain.Indirect;

namespace Domain.Indirect.AllowedFrom
{
	public class CallInstanceMethodIndirect_SuperSuperClass : BaseIndirect
	{
		public void MethodOfSuperClass()
		{
			subSubDao.MethodOnSuperClass();
		}
	}
}
