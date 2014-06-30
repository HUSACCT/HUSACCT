package infrastructure.socialmedia.twitter;

import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
//Functional requirement 2.3
//Test case 8: Only class infrastructure.socialmedia.twitter.LocationTrends is only allowed to use the twitter4j.jar library file
//Result: FALSE
class LocationTrends {
	String[] AllDailyTrends(){
		String woeid = "727232"; //woeid of Amsterdam
		try {
			Twitter twitter = new TwitterFactory().getInstance();
			Trends trends = twitter.getLocationTrends(Integer.parseInt(woeid));
			String[] returnarray = new String[trends.getTrends().length];
			return returnarray;
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get location trends: " + te.getMessage());
		}
		return new String[0];
	}
}