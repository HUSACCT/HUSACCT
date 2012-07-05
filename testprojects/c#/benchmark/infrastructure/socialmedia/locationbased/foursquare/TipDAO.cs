using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare
{
    //Functional requirement 3.2
    //Test case 140: Class domain.locationbased.foursquare.Tip is not allowed to use enumeration infrastructure.socialmedia.locationbased.foursquare.TipDAO
    //Result: FALSE
    public enum TipDAO
    {
        ONE, TWO, THREE, FOUR
    }
}