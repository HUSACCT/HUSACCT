using Technology.Direct.Dao;

namespace Domain.Direct.Violating
{
	public class AccessInnerEnumeration
	{
		public AccessInnerEnumeration()
		{
			InnerEnumeration e = CallInstanceOuterClassDAO.InnerEnumeration.ONE;
		}
	}
}
