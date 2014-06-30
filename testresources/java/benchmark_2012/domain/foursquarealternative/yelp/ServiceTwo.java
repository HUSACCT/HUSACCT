package domain.foursquarealternative.yelp;

import java.util.Calendar;

//Functional requirement 3.2
//Test case 149 + 153: Domain.foursquarealternative.brightkite is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
//Result: FALSE
//public interface IService{
public class ServiceTwo{
	public static String sName = "ServiceTwo";
	public String name;
	public Calendar day;
	
	public ServiceTwo() {
		name = "ServiceTwo";
		day = Calendar.getInstance();
	}
	public String getName(){
		return name;
	}
	
	public Calendar getDay(){
		return day;
	}
}