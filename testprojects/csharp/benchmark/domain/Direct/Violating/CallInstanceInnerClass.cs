namespace Domain.Direct.Violating
{
	public class CallInstanceInnerClass : Base
	{
		public int CallMethodInstanceInnerClass()
		{
			return innerDao.GetNext();
		}
	}
}
