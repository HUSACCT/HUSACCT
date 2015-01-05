using Technology.Direct.Dao;

namespace Domain.Direct.Allowed
{
	public class AccessInnerEnumeration
	{
		public AccessInnerEnumeration()
		{
			InnerEnumeration e = CallInstanceOuterClassDAO.InnerEnumeration.ONE;
		}
	}
}
