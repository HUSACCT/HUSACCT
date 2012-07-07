using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.foursquarealternative.yelp;

namespace CSharpBenchmark.domain.foursquarealternative.whrrl
{
    //Functional requirement 3.2
    //Test case 150: Class domain.foursquarealternative.kilroy.Map is not allowed to have a dependency with classes from domain.foursquarealternative.glympse
    //Result: TRUE
    public class FrontService
    {
        public static IYelp service;
    }
}