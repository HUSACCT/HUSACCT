namespace Domain.Direct.Violating
{
	using Domain.Direct;	

	public class AccessInstanceVariableWrite : Base
	{
		public AccessInstanceVariable()
		{
			String s = "profit";
			profileDao.name = s;
		}
	}
}
