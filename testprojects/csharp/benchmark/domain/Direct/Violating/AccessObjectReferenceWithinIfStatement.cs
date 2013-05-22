namespace Domain.Direct.Violating
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
