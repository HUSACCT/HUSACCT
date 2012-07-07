using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.foursquarealternative.whrrl;

namespace CSharpBenchmark.domain.foursquarealternative.kilroy
{
    //Functional requirement 3.2
    //Test case 154: Class domain.foursquarealternative.kilroy.Future is not allowed to have a dependency with classes from domain.foursquarealternative.glympse
    //Result: TRUE
    public class Future
    {
        public Future()
        {
            new WhrrlFuture().getFuture();
        }
    }
}