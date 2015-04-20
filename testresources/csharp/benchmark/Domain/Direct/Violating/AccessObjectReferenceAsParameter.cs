namespace Domain.Direct.Violating
{
	using Domain.Direct;	
	using Technology.Direct.Dao;

	public class AccessObjectReferenceAsParameter
	{
		private DeclarationParameter declaration;
		ProfileDAO profileDao;

		public AccessObjectReferenceAsParameter()
		{
			declaration.GetProfileInformation(profileDao);
		}
	}
}
