package domain.direct.violating;

import fi.foyt.foursquare.api.FoursquareApi;

public class InheritanceExtendsLibraryClass extends FoursquareApi{

	public InheritanceExtendsLibraryClass(String clientId, String clientSecret, String redirectUrl) {
		super(clientId, clientSecret, redirectUrl);
		// TODO Auto-generated constructor stub
	}

	public void handleCallback() {
			boolean b = true;
	}
}