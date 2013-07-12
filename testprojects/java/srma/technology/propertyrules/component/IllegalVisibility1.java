package technology.propertyrules.component;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class IllegalVisibility1 {

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