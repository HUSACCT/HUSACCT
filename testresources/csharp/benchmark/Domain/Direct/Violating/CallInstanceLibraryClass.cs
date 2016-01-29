using Domain.Direct;


namespace Domain.Direct.Violating
{
	using FI.Foyt.Foursquare.Api.FoursquareApi;

	public class CallInstanceLibraryClass : Base
	{
		protected internal FoursquareApi fourApi;

		public void HandleCallback()
		{
			fourApi.GetAuthenticationUrl();
		}
	}
}
