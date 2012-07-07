using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare
{
    //Functional requirement 3.2
    //Test case 138: Class domain.locationbased.foursquare.Statics is not allowed to use exception infrastructure.socialmedia.locationbased.foursquare.FourSqaureException 
    //Result: FALSE
    public class FourSquareException : Exception
    {
        public FourSquareException(String message)
            : base(message)
        {

        }
    }
}