package infrastructure.socialmedia.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
//Functional requirement 2.3
//Test case 8: Only class infrastructure.socialmedia.twitter.LocationTrends is only allowed to use the twitter4j.jar library file
//Result: FALSE
public class LegalInformation {

	public String getTermsOfService(){
		try {
			Twitter twitter = new TwitterFactory().getInstance();
			return twitter.getTermsOfService();
		} catch (TwitterException te) {
			te.printStackTrace();
			return "Failed to get tems of service: " + te.getMessage();
		}
	}

	public String getPrivacyPolicy(){
		try {
			Twitter twitter = new TwitterFactory().getInstance();
			return twitter.getPrivacyPolicy();
		} catch (TwitterException te) {
			te.printStackTrace();
			return "Failed to get privacy policy: " + te.getMessage();
		}
	}
}