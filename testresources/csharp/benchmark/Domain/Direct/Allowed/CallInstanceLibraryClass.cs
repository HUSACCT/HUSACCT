namespace Domain.Direct.Allowed
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
