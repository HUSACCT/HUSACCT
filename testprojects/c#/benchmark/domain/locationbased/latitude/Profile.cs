using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.latitude;

namespace CSharpBenchmark.domain.locationbased.latitude
{


    //Functional requirement 3.2
    //Test case 131: Class domain.locationbased.latitude.Profile is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.ProfileDAO 
    //Result: TRUE
    public class Profile
    {
        //FR5.5
        public string getProfileInformation(ProfileDAO dao)
        {
            return "";
        }
    }
}