using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.foursquarealternative.whrrl;

namespace CSharpBenchmark.domain.foursquarealternative.brightkite
{
    //Functional requirement 3.2 + 5.1
    //Test case 153: Class domain.foursquarealternative.brightkite.CheckIn is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
    //Result: FALSE
    public class CheckInIndirectInvocation4
    {
        private BackgroundService bckg;
        private String test;

        public CheckInIndirectInvocation4()
        {
            bckg = new BackgroundService();
        }

        public String Case4()
        {
            // Indirect invocation via normal method and with toString()
            test = bckg.getService2().getDay().ToString();
            return test;
        }
    }
}
