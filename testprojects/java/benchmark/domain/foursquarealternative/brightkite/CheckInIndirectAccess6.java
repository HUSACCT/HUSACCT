package domain.foursquarealternative.brightkite;

import domain.foursquarealternative.whrrl.BackgroundService;

//Functional requirement 3.2 + 5.2
//Test case 149: Class domain.foursquarealternative.brightkite.CheckIn is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
//Result: FALSE

public class CheckInIndirectAccess6 {
	private BackgroundService bckg;
	private String test;

	public CheckInIndirectAccess6(){
		bckg = new BackgroundService();
	}
		// 149.6 Indirect access via static attribute and without toString()
		public String case6() {
			test = (String) bckg.service2.sName;
			return test;
		}
}
