namespace Domain.Indirect.ViolatingFrom
{
	public class CallInstanceMethodIndirect_SuperSuperClass : BaseIndirect
	{
		public void MethodOfSuperClass()
		{
			subSubDao.MethodOnSuperClass();
		}
	}
}
