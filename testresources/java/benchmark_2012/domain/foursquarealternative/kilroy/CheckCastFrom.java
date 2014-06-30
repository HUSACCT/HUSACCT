package domain.foursquarealternative.kilroy;

import domain.foursquarealternative.SameExtend;
import domain.foursquarealternative.whrrl.CheckCastTo;
//Functional requirement 3.2 + Type conversion
//Test case 155: Class domain.foursquarealternative.brightkite.CheckIn is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
//Result: FALSE
public class CheckCastFrom extends SameExtend {
	//private int i;
	public CheckCastFrom(){
		Object o = (CheckCastTo) new SameExtend();
	}
}