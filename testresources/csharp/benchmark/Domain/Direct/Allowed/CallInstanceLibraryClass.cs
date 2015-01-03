namespace Domain.Direct.Violating
{
	using Domain.Direct;

	public class CallInstanceLibraryClass : Base
	{
		public void HandleCallback()
		{
			fourApi.GetAuthenticationUrl();
		}
	}
}
