using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare
{
    //Functional requirement 3.2
    //Test case 118: Class domain.locationbased.foursquare.Badges is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.BadgesDAO 
    //Result: FALSE
    public class BadgesDAO
    {
        public static string[] getAllBadges()
        {
            return new string[] { "badge1", "badge2" };
        }
    }
}