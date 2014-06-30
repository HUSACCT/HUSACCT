namespace Domain.Direct.Violating
{
	public class AccessInstanceVariableSuperClass : Domain.Direct.Base
	{
		public String Method()
		{
			return SubDao.VariableOnSuperClass;
		}
	}
}
