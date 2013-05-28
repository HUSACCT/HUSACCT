namespace Domain.Indirect.AllowedFrom
{
	public class CallInstanceMethodIndirect_SuperClass : BaseIndirect
	{
		public void MethodOfSuperClass()
		{
			subDao.MethodOnSuperClass();
		}
	}
}
