namespace Domain.Direct.Allowed
{
	using Domain.Direct;

	public class CallInstanceInnerClass : Base
	{
		public int CallMethodInstanceInnerClass()
		{
			return innerDao.GetNext();
		}
	}
}
