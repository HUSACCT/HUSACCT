namespace Domain.Direct.Violating
{
	using Domain.Direct;	
	using Technology.Direct.Dao;

	public class AccessObjectReferenceWithinIfStatement
	{
		private ProfileDAO profileDao;

		public AccessObjectReferenceWithinIfStatement()
		{
			if (profileDao != null)
			{
                string s = "wrong";
			}
		}
	}
}
