package domain.direct.violating;

import technology.direct.dao.*;
import fi.foyt.foursquare.api.FoursquareApi;

public class InheritanceExtends extends HistoryDAO implements IMapDAO {
	
	HashMap<String, ProfileDAO> hashMap;
	FoursquareApi[] fss;

}
