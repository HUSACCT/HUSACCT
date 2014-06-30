package infrastructure.socialmedia.twitter;

import java.util.List;
//Functional requirement 2.3
//Test case 8: All classes in package infrastructure.socialmedia.twitter have visibility package or lower, except for class: infrastructure.socialmedia.twitter.TwitterFacade
//Result: TRUE
public class TwitterFacade {
	private SearchTweets searchTweets;
	private SearchPlaces searchPlaces;
	private LocationTrends locationTrends;
	private LegalInformation legalInformation;

	public TwitterFacade(){
		this.searchTweets = new SearchTweets();
		this.searchPlaces = new SearchPlaces();
		this.locationTrends = new LocationTrends();
		this.legalInformation = new LegalInformation();
	}

	public String getTermsOfService(){
		return legalInformation.getTermsOfService();
	}

	public String getPrivacyPolicy(){
		return legalInformation.getPrivacyPolicy();
	}

	public String[] AllDailyTwitterTrends(){
		return locationTrends.AllDailyTrends();
	}

	public List<String> getPlaceNameGeo(double latitude, double longitude){
		return searchPlaces.getPlaceNameGeo(latitude, longitude);
	}

	public List<String> getPlaceNameGeo(String ipaddress){
		return searchPlaces.getPlaceNameGeo(ipaddress);
	}

	public List<String> searchTweets(String keyword){
		return searchTweets.searchTweets(keyword);
	}
}