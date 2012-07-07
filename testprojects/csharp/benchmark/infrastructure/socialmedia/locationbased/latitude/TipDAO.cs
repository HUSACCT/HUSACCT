using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.socialmedia.locationbased.latitude
{
    //Functional requirement 3.2
    //Test case 139: Class domain.locationbased.latitude.Tip is not allowed to use enumeration infrastructure.socialmedia.locationbased.foursqaure.TipDAO
    //Result: TRUE
    public enum TipDAO
    {
        ONE, TWO, THREE, FOUR
    }
}