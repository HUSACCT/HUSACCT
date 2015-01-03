namespace Domain.Direct.Violating
{
	public class AccessInstanceVariableSuperSuperClass : Domain.Direct.Base
	{
		public String Method()
		{
			return subSubDao.VariableOnSuperClass;
		}
	}
}
