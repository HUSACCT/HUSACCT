using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.foursquarealternative.whrrl;

namespace CSharpBenchmark.domain.foursquarealternative.brightkite
{
    //Functional requirement 3.2 + 5.1 + 5.9
    //Test case 153: Class domain.foursquarealternative.brightkite.CheckIn is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
    //Result: FALSE
    public class CheckInIndirectInvocation3
    {
        private BackgroundService bckg;
        private String test;

        public CheckInIndirectInvocation3()
        {
            bckg = new BackgroundService();
        }

        // Indirect access via normal method and with type cast
        public String case3()
        {
            test = (String)bckg.service2.getName();
            return test;
        }
    }
}
