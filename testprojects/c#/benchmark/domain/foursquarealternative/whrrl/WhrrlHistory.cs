using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.foursquarealternative.yelp;

namespace CSharpBenchmark.domain.foursquarealternative.whrrl
{
    //Functional requirement 3.2
    //Test case 153: Class domain.foursquarealternative.brightkite.History is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
    //Result: FALSE
    public class WhrrlHistory
    {
        public MyHistory getHistory()
        {
            return null;
        }
    }
}