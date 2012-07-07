using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare
{
    //Functional requirement 3.2
    //Test case 122: Class domain.locationbased.foursquare.CheckIn is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.CheckInDAO 
    //Result: FALSE
    public class CheckInDAO
    {
        public static string currentLocation = "Nijenoord 1, Utrecht";
    }
}