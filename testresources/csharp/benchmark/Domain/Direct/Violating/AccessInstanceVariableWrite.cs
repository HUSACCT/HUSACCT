namespace Domain.Direct.Violating
{
	public class AccessInstanceVariable : Base
	{
		public AccessInstanceVariable()
		{
			String s = "profit";
			profileDao.name = s;
		}
	}
}
