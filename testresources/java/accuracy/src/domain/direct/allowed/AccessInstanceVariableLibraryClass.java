package domain.direct.allowed;

import fi.foyt.foursquare.api.FoursquareApiException;

public class AccessInstanceVariableLibraryClass {
	private FoursquareApiException fourExc;
	
	// Note message is no variable of fourExc, but it is refereed here since no public variable is available. 
	public void handleCallback() {
		String s = fourExc.message;
	}
}