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

    public class CheckInIndirectAccess7
    {
        //private BackgroundService bckg;
        private String test;

        public CheckInIndirectAccess7()
        {
            //bckg = new BackgroundService();
        }
        // 149.7 Indirect access via static method and toString
        public String case7()
        {
            test = BackgroundService.service1.ToString();
            return test;
        }
    }
}
