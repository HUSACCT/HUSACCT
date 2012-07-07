using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia;

namespace CSharpBenchmark.domain.shortcharacter.twitter
{
    //Functional requirement 3.1.1
    //Test case 38: Class domain.shortcharacter.twitter.AdvertiseTweet may only have a dependency with class have a dependency with class infrastructure.socialmedia.SocialNetworkInfo 
    //Result: TRUE
    public class AdvertiseTweet
    {
        public AdvertiseTweet()
        {
            //FR5.1
            Console.WriteLine(SocialNetworkInfo.getInfo());
        }
    }
}