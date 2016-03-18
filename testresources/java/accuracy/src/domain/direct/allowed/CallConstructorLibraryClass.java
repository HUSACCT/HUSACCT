package domain.direct.allowed;

import fi.foyt.foursquare.api.FoursquareApi;

public class CallConstructorLibraryClass {
	public void handleCallback() {
			new FoursquareApi("Client ID", "Client Secret", "Callback URL");

	}
}