package infrastructure.socialmedia.locationbased.foursquare;
//Functional requirement 3.2
//Test case 118: Class domain.locationbased.foursquare.Badges is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.BadgesDAO 
//Result: FALSE
public class BadgesDAO {
	public static String[] getAllBadges() {
		return new String[] {"badge1", "badge2"};
	}
}