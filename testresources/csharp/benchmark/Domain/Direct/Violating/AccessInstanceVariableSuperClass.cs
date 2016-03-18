using Technology.Direct.Subclass;

namespace Domain.Direct.Violating
{
	public class AccessInstanceVariableSuperClass
	{
		private CallInstanceSubClassDOA subDao;

		public String Method()
		{
			return subDao.VariableOnSuperClass;
		}
	}
}
