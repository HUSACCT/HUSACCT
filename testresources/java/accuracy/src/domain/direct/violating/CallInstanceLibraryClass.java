package domain.direct.violating;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;

public class CallInstanceLibraryClass {
	private FoursquareApi fourApi;
	
	public void handleCallback() {
				fourApi.getAuthenticationUrl();
	}
}