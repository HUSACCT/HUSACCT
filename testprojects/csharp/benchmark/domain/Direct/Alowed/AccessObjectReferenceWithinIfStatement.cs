namespace Domain.Direct.Allowed
{
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
