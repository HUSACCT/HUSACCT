package infrastructure.socialmedia.twitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
//Functional requirement 2.3
//Test case 8: Only class infrastructure.socialmedia.twitter.LocationTrends is only allowed to use the twitter4j.jar library file
//Result: FALSE
class SearchTweets implements SearchTweetsMethods {
	
	List<String> searchTweets(String keyword){
		if(keyword != null){			
			Twitter twitter = new TwitterFactory().getInstance();
			try {
				QueryResult result = twitter.search(new Query(keyword));
				List<Tweet> tweets = result.getTweets();

				if(tweets.isEmpty()){
					return Collections.emptyList();
				}
				else{
					List<String> tweetList = new ArrayList<String>();
					for (Tweet tweet : tweets) {
						tweetList.add("@" + tweet.getFromUser() + " - " + tweet.getText());
					}
					return tweetList;
				}
			} catch (TwitterException te) {
				te.printStackTrace();
				System.out.println("Failed to search tweets: " + te.getMessage());
			}
		}
		return Collections.emptyList();
	}
}