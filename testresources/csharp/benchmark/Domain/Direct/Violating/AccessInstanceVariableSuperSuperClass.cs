using Technology.Direct.Subclass;

namespace Domain.Direct.Violating
{
	public class AccessInstanceVariableSuperSuperClass
	{
		private CallInstanceSubSubClassDOA subSubDao;

		public String Method()
		{
			return subSubDao.VariableOnSuperClass;
		}
	}
}
