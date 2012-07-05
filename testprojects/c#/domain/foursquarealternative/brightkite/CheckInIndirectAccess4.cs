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

    public class CheckInIndirectAccess4
    {
        private BackgroundService bckg;
        private String test;

        public CheckInIndirectAccess4()
        {
            bckg = new BackgroundService();
        }
        // 149.4 Indirect-indirect access of a normal attribute 
        public String case4()
        {
            test = bckg.service2.serviceTwo.name;
            return test;
        }
    }		
}
