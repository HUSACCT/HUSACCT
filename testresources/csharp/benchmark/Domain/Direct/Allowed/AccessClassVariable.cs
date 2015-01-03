
namespace Domain.Direct.Violating
{
	public class AccessClassVariable
	{
		public AccessClassVariable()
		{
            string s = Technology.Direct.Dao.CheckInDAO.currentLocation;
		}
	}
}
