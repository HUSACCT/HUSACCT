namespace Domain.Direct.Allowed
{
	using Domain.Direct;	

	public class AccessObjectReferenceWithinIfStatement : Base
	{
		public AccessObjectReferenceWithinIfStatement()
		{
			if (profileDao != null)
			{
                string s = "wrong";
			}
		}
	}
}
