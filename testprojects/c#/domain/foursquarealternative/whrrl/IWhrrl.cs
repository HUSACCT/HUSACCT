using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.foursquarealternative.yelp;

namespace CSharpBenchmark.domain.foursquarealternative.whrrl
{
    //Functional requirement 3.2
    //Test case 147: Class domain.foursquarealternative.brightkite.Account is not allowed to have a dependency with classes in package domain.foursquarealternative.yelp
    //Result: FALSE

    public interface IWhrrl : IPreferences
    {

    }
}
