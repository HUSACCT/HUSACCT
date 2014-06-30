package domain.foursquarealternative.brightkite;

import domain.foursquarealternative.whrrl.BackgroundService;

//Functional requirement 3.2 + 5.1
//Test case 153: Class domain.foursquarealternative.brightkite.CheckIn is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
//Result: FALSE
public class CheckInIndirectInvocation5 {
	//private BackgroundService bckg;
	//private String test;
	
	public CheckInIndirectInvocation5() {
	//	bckg = new BackgroundService();
	}

	public void case5(){
		// Indirect invocation via static method with println and toString()
		System.out.println(BackgroundService.getService1().toString());
	}
}