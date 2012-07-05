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
    public class ServiceOne
    {
        public static string sName;
        public static string name;
        public DateTime day;
        public ServiceTwo serviceTwo;

        public ServiceOne()
        {
            name = "ServiceOne";
            sName = "Service";
            day = new DateTime();
        }

        public string getName()
        {
            return name;
        }

        public DateTime getDay()
        {
            return day;
        }

    }
}
