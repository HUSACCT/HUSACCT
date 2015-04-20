namespace Domain.Direct.Violating
{
	using Domain.Direct;
	using Technology.Direct.Dao;

	public class CallInstanceInnerClass
	{
		CallInstanceOuterClassDAO.CallInstanceInnerClassDAO innerDao;

		public int CallMethodInstanceInnerClass()
		{
			return innerDao.GetNext();
		}
	}
}
