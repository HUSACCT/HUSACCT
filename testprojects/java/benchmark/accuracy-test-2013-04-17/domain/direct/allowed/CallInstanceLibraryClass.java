package domain.direct.allowed;

import domain.direct.Base;
//import fi.foyt.foursquare.api.FoursquareApiException;

public class CallInstanceLibraryClass extends Base{
	public void handleCallback() {
//		try {
//			String code = "1234567890";
				fourApi.getAuthenticationUrl();
			//			fourApi.authenticateCode(code);

//		} catch (FoursquareApiException e) {

//		}
	}
}