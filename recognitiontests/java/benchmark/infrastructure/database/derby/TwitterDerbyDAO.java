package infrastructure.database.derby;

import java.util.Collections;
import java.util.List;

import twitter4j.TwitterFactory;
//Functional requirement 2.3
//Test case 5: Only the classes in package infrastructure.socialmedia.twitter are allowed to use the twitter4j.jar library file.
//Result: FALSE
public class TwitterDerbyDAO implements ISavedTweets {

	public List<String> getSavedTweets(String country){
		getWoeidByCountryName(country);
		return Collections.emptyList();
	}
	
	private int getWoeidByCountryName(String country){
		new TwitterFactory().getInstance();
		return 0;
	}
}