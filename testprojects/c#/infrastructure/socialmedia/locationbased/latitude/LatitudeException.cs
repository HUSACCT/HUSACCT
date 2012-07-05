using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.socialmedia.locationbased.latitude
{
    //Functional requirement 3.2
    //Test case 137: Class domain.locationbased.latitude.Statics is not allowed to use exception infrastructure.socialmedia.locationbased.foursquare.FourSquareException
    //Result: TRUE
    public class LatitudeException : Exception
    {
        public LatitudeException(String message) :
            base(message)
        {
        }
    }
}