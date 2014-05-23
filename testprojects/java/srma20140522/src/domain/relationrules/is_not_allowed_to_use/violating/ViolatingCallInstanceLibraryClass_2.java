package domain.relationrules.is_not_allowed_to_use.violating;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;

public class ViolatingCallInstanceLibraryClass_2 {
	private FoursquareApi fourApi;
	public void handleCallback() {
		try {
			String code = "1234567890";
			fourApi.authenticateCode(code);

		} catch (FoursquareApiException e) {

		}
	}
}