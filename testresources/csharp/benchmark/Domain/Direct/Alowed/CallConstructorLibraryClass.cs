using FI.Foyt.Foursquare.Api.FoursquareApi;

namespace Domain.Direct.Violating
{
	public class CallConstructorLibraryClass
	{
		public virtual void HandleCallback()
		{
            new FoursquareApi("Client ID", "Client Secret", "Callback URL");
		}
	}
}
