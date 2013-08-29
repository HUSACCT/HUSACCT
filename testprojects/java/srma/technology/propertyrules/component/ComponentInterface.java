package technology.propertyrules.component;

import java.util.List;

public class ComponentInterface {
	private SearchTweets searchTweets;
	private SearchPlaces searchPlaces;
	private LocationTrends locationTrends;
	private IllegalVisibility1 legalInformation;

	public ComponentInterface(){
		this.searchTweets = new SearchTweets();
		this.searchPlaces = new SearchPlaces();
		this.locationTrends = new LocationTrends();
		this.legalInformation = new IllegalVisibility1();
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