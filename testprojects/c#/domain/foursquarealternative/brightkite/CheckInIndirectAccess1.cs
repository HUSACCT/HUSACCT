using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.foursquarealternative.whrrl;
using CSharpBenchmark.domain.foursquarealternative.yelp;

namespace CSharpBenchmark.domain.foursquarealternative.brightkite
{

    //Functional requirement 3.2 + 5.2
    //Test case 149: Class domain.foursquarealternative.brightkite.CheckIn is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
    //Result: FALSE

    public class CheckInIndirectAccess1
    {
        private BackgroundService bckg;
        private string test;

        public CheckInIndirectAccess1()
        {
            bckg = new BackgroundService();
        }
        // 149.1 Indirect access of a normal attribute 
        public String case1()
        {
            test = ServiceOne.sName;
            return test;
        }
    }
}
