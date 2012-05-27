package domain.foursquarealternative.brightkite;

import domain.foursquarealternative.whrrl.BackgroundService;

//Functional requirement 3.2 + 5.2
//Test case 149: Class domain.foursquarealternative.brightkite.CheckIn is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
//Result: FALSE

public class CheckInIndirectAccess5 {
	private BackgroundService bckg;
	private String test;

	public CheckInIndirectAccess5(){
		bckg = new BackgroundService();
	}
		// 149.5 Indirect access of a static attribute
		public String case5() {
			test = bckg.service2.sName;
			return test;
		}
}
