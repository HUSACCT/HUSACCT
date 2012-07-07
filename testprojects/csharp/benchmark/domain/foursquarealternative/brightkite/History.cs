using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.foursquarealternative.whrrl;

namespace CSharpBenchmark.domain.foursquarealternative.brightkite
{
    //Functional requirement 3.2
    //Test case 153: Class domain.foursquarealternative.brightkite.History is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
    //Replaced by CheckInIndirectInvocation
    //Result: FALSE
    public class History
    {
        public History()
        {
            //	new WhrrlHistory().getHistory();
        }
    }
}