namespace Domain.Direct.Violating
{
	using Domain.Direct;	

	public class AccessObjectReferenceAsParameter : Base
	{
		private DeclarationParameter declaration;

		public AccessObjectReferenceAsParameter()
		{
			declaration.GetProfileInformation(profileDao);
		}
	}
}
