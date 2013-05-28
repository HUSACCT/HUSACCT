namespace Domain.Direct.Violating
{
	public class CallConstructorLibraryClass
	{
		public virtual void HandleCallback()
		{
			new FI.Foyt.Foursquare.Api.FoursquareApi("Client ID", "Client Secret", "Callback URL");
		}
	}
}
