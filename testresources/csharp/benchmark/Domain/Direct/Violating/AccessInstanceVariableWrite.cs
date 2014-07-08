namespace Domain.Direct.Violating
{
	using Domain.Direct;	

	public class AccessInstanceVariable : Base
	{
		public AccessInstanceVariable()
		{
			String s = "profit";
			profileDao.name = s;
		}
	}
}
