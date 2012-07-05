using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare;

namespace CSharpBenchmark.domain.locationbased.foursquare
{
    //Functional requirement 3.2
    //Test case 126: Class domain.locationbased.foursquare.Friends is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.FriendsDAO 
    //Result: FALSE

    //FR5.3
    public class Friends : FriendsDAO
    {

    }
}