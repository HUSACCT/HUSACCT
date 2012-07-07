using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.foursquarealternative.yelp;
using CSharpBenchmark.domain.foursquarealternative;

namespace CSharpBenchmark.domain.foursquarealternative.brightkite
{
    //Functional requirement 3.2 + Type conversion
    //Test case 155: Class domain.foursquarealternative.brightkite.CheckIn is not allowed to have a dependency with classes from domain.foursquarealternative.yelp
    //Result: FALSE
    public class CheckCastFrom : SameExtend
    {
        //private int i;
        public CheckCastFrom()
        {   
            //	i = (int) CheckCastTo.d;
            Object o = (CheckCastTo)new SameExtend();
        }
    }
}