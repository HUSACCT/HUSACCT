package domain.direct.violating;

import fi.foyt.foursquare.api.FoursquareApi;

public class CallConstructorLibraryClass {
	public void handleCallback() {
			new FoursquareApi("Client ID", "Client Secret", "Callback URL");

	}
}