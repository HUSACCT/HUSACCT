using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.domain.foursquarealternative.yelp
{

    //Functional requirement 3.2
    //Test case 149 + 153: Domain.foursquarealternative.brightkite is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
    //Result: FALSE
    //public interface IService{
    public class ServiceTwo
    {
        public static String sName = "ServiceTwo";
        public String name;
        public DateTime day;

        public ServiceTwo()
        {
            name = "ServiceTwo";
            day = new DateTime();
        }
        public String getName()
        {
            return name;
        }

        public DateTime getDay()
        {
            return day;
        }
    }
}
