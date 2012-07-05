using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.latitude;

namespace CSharpBenchmark.domain.locationbased.latitude
{

    //Functional requirement 3.2
    //Test case 129: Class domain.locationbased.latitude.Map is not allowed to use interface infrastructure.socialmedia.locationbased.foursquare.IMap
    //Result: TRUE

    //FR5.4
    public class Map : IMap
    {

    }
}
