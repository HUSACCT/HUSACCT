package domain.foursquarealternative.brightkite;

import domain.foursquarealternative.whrrl.BackgroundService;

//Functional requirement 3.2 + 5.2 + 5.9
//Test case 149: Class domain.foursquarealternative.brightkite.CheckIn is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
//Result: FALSE

public class CheckInIndirectAccess2 {
	private BackgroundService bckg;
	private String test;

	public CheckInIndirectAccess2(){
		bckg = new BackgroundService();
	}
		// 149.2 Indirect access of a normal attribute and with type cast
		public String case2() {
			test = (String) bckg.service2.name;
			return test;
		}
}
