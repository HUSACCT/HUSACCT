package domain.direct.violating;

import fi.foyt.foursquare.api.FoursquareApiException;

public class AccessInstanceVariableLibraryClass {
	private FoursquareApiException fourExc;
	
	public void handleCallback() {
				String s = fourExc.message;
	}
}