namespace Domain.Direct.Allowed
{
	public class AccessInstanceVariableSuperClass : Domain.Direct.Base
	{
		public String Method()
		{
			return subDao.VariableOnSuperClass;
		}
	}
}
