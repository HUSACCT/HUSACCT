using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare;

namespace CSharpBenchmark.domain.locationbased.foursquare
{

    //Functional requirement 3.2
    //Test case 132: Class domain.locationbased.foursquare.Profile is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.ProfileDAO 
    //Result: FALSE
    public class Profile
    {
        //FR5.5
        public string getProfileInformation(ProfileDAO dao)
        {
            return "";
        }
    }
}