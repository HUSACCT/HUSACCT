using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.foursquarealternative.whrrl;

namespace CSharpBenchmark.domain.foursquarealternative.brightkite
{

    //Functional requirement 3.2 + 5.2
    //Test case 149: Class domain.foursquarealternative.brightkite.CheckIn is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
    //Result: FALSE

    public class CheckInIndirectAccess5
    {
        private BackgroundService bckg;
        private String test;

        public CheckInIndirectAccess5()
        {
            bckg = new BackgroundService();
        }
        // 149.5 Indirect access of a static attribute
        public String case5()
        {
            // This testcase is not available in C#. It's not possible to use references with static attributes.
            //test = bckg.service2.sName;
            return test;
        }
    }	
}
