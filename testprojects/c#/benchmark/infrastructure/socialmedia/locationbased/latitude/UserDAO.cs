using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.socialmedia.locationbased.latitude
{

    //Functional requirement 3.2
    //Test case 141: Class domain.locationbased.latitude.User is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.UserDAO
    //Result: TRUE
    public class UserDAO
    {
        public static readonly String name = "username";
        public readonly String message = "The social information";

        public UserDAO()
        {

        }
    }
}