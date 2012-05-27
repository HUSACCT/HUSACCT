package domain.foursquarealternative.brightkite;

import domain.foursquarealternative.SameExtend;
import domain.foursquarealternative.yelp.CheckCastTo;
//Functional requirement 3.2 + Type conversion
//Test case 155: Class domain.foursquarealternative.brightkite.CheckIn is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
//Result: FALSE
public class CheckCastFrom extends SameExtend {
	//private int i;
	public CheckCastFrom(){
	//	i = (int) CheckCastTo.d;
		Object o = (CheckCastTo) new SameExtend();
	}
}