using FI.Foyt.Foursquare.Api;
using Technology.Direct.Dao;
using Technology.Direct.Subclass;

namespace Domain.Direct
{
	public class Base
	{
		protected internal UserDAO userDao;

		protected internal ProfileDAO profileDao;

		protected internal CallInstanceOuterClassDAO.CallInstanceInnerClassDAO innerDao;

		protected internal CallInstanceInterfaceDAO interfaceDao;

		protected internal FoursquareApi fourApi;

		protected internal CallInstanceSubClassDOA subDao;

		protected internal CallInstanceSubSubClassDOA subSubDao;

		public Base()
		{
		}
	}
}
