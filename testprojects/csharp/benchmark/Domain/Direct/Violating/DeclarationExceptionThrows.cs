namespace Domain.Direct.Violating
{
	public class DeclarationExceptionThrows
	{
		public string GetStatics()
		{
            throw new Technology.Direct.Dao.StaticsException("No statics available");
		}
	}
}
