package domain.foursquarealternative.brightkite;

import domain.foursquarealternative.whrrl.BackgroundService;

//Functional requirement 3.2 + 5.2
//Test case 149: Class domain.foursquarealternative.brightkite.CheckIn is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
//Result: FALSE

public class CheckInIndirectAccess1 {
	private BackgroundService bckg;
	private String test;

	public CheckInIndirectAccess1(){
		bckg = new BackgroundService();
	}
		// 149.1 Indirect access of a normal attribute 
		public String case1() {
			test = bckg.service2.name;
			return test;
		}
}
