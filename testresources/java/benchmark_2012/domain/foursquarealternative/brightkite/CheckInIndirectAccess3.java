package domain.foursquarealternative.brightkite;

import domain.foursquarealternative.whrrl.BackgroundService;

//Functional requirement 3.2 + 5.2
//Test case 149: Class domain.foursquarealternative.brightkite.CheckIn is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
//Result: FALSE

public class CheckInIndirectAccess3 {
	private BackgroundService bckg;
	private String test;

	public CheckInIndirectAccess3(){
		bckg = new BackgroundService();
	}
		// 149.3 Indirect access of a normal attribute and with toString()
		public String case3() {
			test = bckg.service2.day.toString();
			return test;
		}
}
