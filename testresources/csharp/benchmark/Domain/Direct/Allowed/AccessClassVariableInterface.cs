namespace Domain.Direct.Allowed
{
	public class AccessClassVariableInterface
	{
		public string sniName;

		public virtual void TestAccessFinalAttribute()
		{
            sniName = Technology.Direct.Dao.ISierraDAO.NAME;
		}
	}
}
