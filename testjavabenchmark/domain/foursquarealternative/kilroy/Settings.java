package domain.foursquarealternative.kilroy;

import domain.foursquarealternative.whrrl.ISee;
import domain.foursquarealternative.whrrl.WhrrlSettings;
//Functional requirement 3.2
//Test case 148: Class domain.foursquarealternative.kilroy.Settings is not allowed to have a dependency with classes from domain.foursquarealternative.glympse
//Result: TRUE
public class Settings implements ISee {
	public Settings(){
		new WhrrlSettings();
	}
}
