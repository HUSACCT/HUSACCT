namespace Domain.Direct.Allowed
{
	public class CallInstanceInnerClass : Base
	{
		public int CallMethodInstanceInnerClass()
		{
			return innerDao.GetNext();
		}
	}
}
