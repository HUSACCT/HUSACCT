package domain.foursquarealternative.kilroy;

import domain.foursquarealternative.whrrl.FrontService;
//Functional requirement 3.2
//Test case 150: Class domain.foursquarealternative.kilroy.Map is not allowed to have a dependency with classes from domain.foursquarealternative.glympse
//Result: TRUE
public class Map {
	public Map(){
		System.out.println(FrontService.service.toString());
	}
}