using FI.Foyt.Foursquare.Api.FoursquareApi;
using Technology.Direct.Dao;
using Technology.Direct.Subclass;

namespace Domain.Direct
{
	public class Base
	{
		protected internal UserDAO userDao;

		protected internal ProfileDAO profileDao;

		protected internal CallInstanceOuterClassDAO.CallInstanceInnerClassDAO innerDao;

		protected internal CallInstanceOuterClassDAO.CallInstanceInnerInterfaceDAO innerInterfaceDao;		
		
		protected internal CallInstanceInterfaceDAO interfaceDao;

		protected internal FoursquareApi fourApi;

		protected internal CallInstanceSubClassDOA subDao;

		protected internal CallInstanceSubSubClassDOA subSubDao;
		
		protected internal List<ProfileDAO> profileDAOs;


		public Base()
		{
		}
	}
}
