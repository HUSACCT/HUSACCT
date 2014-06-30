package domain.locationbased.foursquare;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
//Functional requirement 3.2
//Test case 146: Class domain.locationbased.foursquare,WebSettings is not allowed to use library foursquareapi.jar
//Result: FALSE
public class WebSettings {
	public void handleCallback() {
		try {
			String code = "1234567890";
			new FoursquareApi("Client ID", "Client Secret", "Callback URL").authenticateCode(code);

		} catch (FoursquareApiException e) {

		}
	}
}