package infrastructure.socialmedia.locationbased.latitude;

//Functional requirement 3.2
//Test case 117: Class domain.locationbased.latitude.Badges is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.BadgesDAO 
//Result: TRUE
public class BadgesDAO {
	public static String[] getAllBadges() {
		return new String[] {"badge1", "badge2"};
	}
}