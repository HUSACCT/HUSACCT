using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Twitterizer;

namespace CSharpBenchmark.infrastructure.socialmedia.twitter
{
    //Functional requirement 2.3
    //Test case 8: All classes in package infrastructure.socialmedia.twitter have visibility package or lower, except for class: infrastructure.socialmedia.twitter.TwitterFacade
    //Result: TRUE
    public class TwitterFacade
    {
        private SearchTweets searchTweetz;
        private SearchPlaces searchPlaces;
        private LocationTrends locationTrends;
        private LegalInformation legalInformation;

        public TwitterFacade()
        {
            this.searchTweetz = new SearchTweets();
            this.searchPlaces = new SearchPlaces();
            this.locationTrends = new LocationTrends();
            this.legalInformation = new LegalInformation();
        }

        public String getTermsOfService()
        {
            return legalInformation.getTermsOfService();
        }

        public String getPrivacyPolicy()
        {
            return legalInformation.getPrivacyPolicy();
        }

        public String[] AllDailyTwitterTrends()
        {
            return locationTrends.AllDailyTrends();
        }

        public List<String> getPlaceNameGeo(double latitude, double longitude)
        {
            return searchPlaces.getPlaceNameGeo(latitude, longitude);
        }

        public List<String> getPlaceNameGeo(String ipaddress)
        {
            return searchPlaces.getPlaceNameGeo(ipaddress);
        }

        public List<String> searchTweets(String keyword)
        {
            return searchTweetz.searchTweets(keyword);
        }
    }

    //Functional requirement 2.3
    //Test case 8: Only class infrastructure.socialmedia.twitter.LocationTrends is only allowed to use the Twitterizer2.dll library file
    //Result: FALSE
    internal class LegalInformation
    {

        public String getTermsOfService()
        {
            TwitterUser user = new TwitterUser();
            return user.ScreenName;
        }

        public String getPrivacyPolicy()
        {
            TwitterUser user = new TwitterUser();
            return user.ScreenName;
        }
    }

    //Functional requirement 2.3
    //Test case 8: Only class infrastructure.socialmedia.twitter.LocationTrends is only allowed to use the Twitterizer2.dll library file
    //Result: FALSE
    internal class LocationTrends
    {
        internal string[] AllDailyTrends()
        {
            TwitterTrendLocation location = new TwitterTrendLocation();
            location.WOEID = 727232; //woeid of Amsterdam

            return new string[0];
        }
    }

    internal class SearchPlaces
    {
        internal List<String> getPlaceNameGeo(double latitude, double longitude)
        {
            TwitterGeo geo = new TwitterGeo();
            Coordinate coord = new Coordinate();
            coord.Latitude = 123;
            coord.Longitude = 12321;
            List<string> placeNames = new List<string>();
            placeNames.Add(coord.ToString());
            placeNames.Add(geo.ToString());

            return placeNames;
        }

        internal List<String> getPlaceNameGeo(string ipaddress)
        {
            TwitterGeo geo = new TwitterGeo();
            Coordinate coord = new Coordinate();
            coord.Latitude = 123;
            coord.Longitude = 12321;
            List<string> placeNames = new List<string>();
            placeNames.Add(ipaddress);

            return placeNames;
        }
    }

    internal class SearchTweets : SearchTweetsMethods
    {

        internal List<string> searchTweets(String keyword)
        {
            List<string> foundTweets = new List<string>();
            string query = "#Twitterizer";
            int pageNumber = 1;

            SearchOptions options = new SearchOptions()
            {
                PageNumber = pageNumber,
                NumberPerPage = 2
            };

            TwitterResponse<TwitterSearchResultCollection> searchResult = TwitterSearch.Search(query, options);

            while (searchResult.Result == RequestResult.Success && pageNumber < 5)
            {
                Console.WriteLine("==== PAGE {0} ====", pageNumber);
                Console.WriteLine();

                foreach (var tweet in searchResult.ResponseObject)
                {

                    Console.WriteLine("[{0}] {1,-10}: {2}", tweet.CreatedDate, tweet.FromUserScreenName, tweet.Text);
                }

                pageNumber++;
                options.PageNumber = pageNumber;
                searchResult = TwitterSearch.Search(query, options);
                Console.WriteLine();
            }
            return foundTweets;
        }
    }

    //Functional requirement 2.5
    //Test case 11: All interfaces in package infrastructure.socialmedia (including all subpackages and subclasses) must have prefix “I”
    //Result: FALSE
    internal interface SearchTweetsMethods
    {
    }
}