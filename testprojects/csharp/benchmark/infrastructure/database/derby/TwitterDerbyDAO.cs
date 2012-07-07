using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Twitterizer;

namespace CSharpBenchmark.infrastructure.database.derby
{
//Functional requirement 2.5
//Test case 10: All interfaces in package infrastructure.database (including all subpackages and subclasses) must have prefix "I"
//Result: TRUE
interface ISavedTweets
{
        List<String> getSavedTweets(string country);
}

//Functional requirement 2.3
//Test case 5: Only the classes in assembly infrastructure.socialmedia.twitter are allowed to use the Twitterizer2.dll library file.
//Result: FALSE
public class TwitterDerbyDAO : ISavedTweets {

	public List<String> getSavedTweets(String country){
		getWoeidByCountryName(country);
		return new List<string>();
	}
	
	private int getWoeidByCountryName(String country){
        // Twitterizer library
        new TwitterGeo();
		return 0;
	}
}
}