package domain.foursquarealternative.brightkite;

//import domain.foursquarealternative.whrrl.BackgroundService;
import domain.foursquarealternative.whrrl.WhrrlHistory;

//Functional requirement 3.2 + 5.1
//Test case 153: Class domain.foursquarealternative.brightkite.CheckIn is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
//Result: FALSE
public class CheckInIndirectInvocation1 {
	//private BackgroundService bckg;
	//private String test;
	
	public CheckInIndirectInvocation1() {
		//bckg = new BackgroundService();
	}

	// 153.1 Indirect invocation via constructor
	public void case1(){
		new WhrrlHistory().getHistory();
	}
}