using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.socialmedia.locationbased.latitude
{

    //Functional requirement 3.2
    //Test case 121: Class domain.locationbased.latitude.CheckIn is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.CheckInDAO 
    //Result: TRUE
    public class CheckInDAO
    {
        public static String currentLocation = "Nijenoord 1, Utrecht";
    }
}