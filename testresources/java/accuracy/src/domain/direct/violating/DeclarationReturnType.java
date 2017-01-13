package domain.direct.violating;

import technology.direct.dao.VenueDAO;
import technology.direct.dao.SettingsAnnotation;

public class DeclarationReturnType {
	@SettingsAnnotation(title = "book")
	public VenueDAO getVenues(){
		return null;
	}
}