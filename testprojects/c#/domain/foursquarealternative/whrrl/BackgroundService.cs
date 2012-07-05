using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.foursquarealternative.yelp;

namespace CSharpBenchmark.domain.foursquarealternative.whrrl
{
    //Functional requirement 3.2
    //Test case 149: Class domain.foursquarealternative.brightkite.CheckIn is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
    //Result: FALSE
    public class BackgroundService
    {
        public static ServiceOne service1;
        public ServiceOne service2;

        public static ServiceOne getService1()
        {
            return service1;
        }
        public ServiceOne getService2()
        {
            return service2;
        }
    }
}