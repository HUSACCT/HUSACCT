namespace Domain.Direct.Violating
{
	public class AccessClassVariableConstant
	{
		public string sniName;

		public virtual void TestAccessStaticFinalAttribute()
		{
            sniName = Technology.Direct.Dao.UserDAO.name;
		}
	}
}
