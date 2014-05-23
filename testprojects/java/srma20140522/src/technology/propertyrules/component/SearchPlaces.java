package technology.propertyrules.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.Place;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

class SearchPlaces {
	List<String> getPlaceNameGeo(double latitude, double longitude){
		GeoQuery query = createGeoQuery(latitude, longitude);
		return performGetPlaceNameGeoQuery(query);
	}
	
	List<String> getPlaceNameGeo(String ipaddress){
		GeoQuery query = createIpQuery(ipaddress);
		return performGetPlaceNameGeoQuery(query);
	}
	
	private List<String> performGetPlaceNameGeoQuery(GeoQuery query){
		try {
			List<String> placename = new ArrayList<String>();
            Twitter twitter = new TwitterFactory().getInstance();  
            ResponseList<Place> places = twitter.searchPlaces(query);
            if (places.size() == 0) {
                System.out.println("No location associated with the specified IP address or lat/lang");
                return Collections.emptyList();
            } else {
                for (Place place : places) {
                	placename.add(place.getFullName());
                    Place[] containedWithinArray = place.getContainedWithIn();
                    if (containedWithinArray != null && containedWithinArray.length != 0) {
                        for (Place containedWithinPlace : containedWithinArray) {
                            placename.add(containedWithinPlace.getFullName());
                            System.out.println(containedWithinPlace.getFullName());
                        }
                    }
                }
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to retrieve places: " + te.getMessage());
        }
		return Collections.emptyList();
	}
	
	private GeoQuery createIpQuery(String ipaddress){
		return new GeoQuery(ipaddress);
	}
	
	private GeoQuery createGeoQuery(double latitude, double longitude){
		return new GeoQuery(new GeoLocation(latitude, longitude));
	}
}