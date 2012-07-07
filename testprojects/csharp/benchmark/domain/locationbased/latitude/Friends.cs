using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.latitude;

namespace CSharpBenchmark.domain.locationbased.latitude
{

    //Functional requirement 3.2
    //Test case 125: Class domain.locationbased.latitude.Friends is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.FriendsDAO  
    //Result: TRUE

    //FR5.3
    public class Friends : FriendsDAO
    {

    }
}