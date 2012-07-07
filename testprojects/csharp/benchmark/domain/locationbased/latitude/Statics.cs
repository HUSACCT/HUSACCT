using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.latitude;

namespace CSharpBenchmark.domain.locationbased.latitude
{

    //Functional requirement 3.2
    //Test case 137: Class domain.locationbased.latitude.Statics is not allowed to use exception infrastructure.socialmedia.locationbased.foursquare.FourSquareException
    //Result: TRUE
    public class Statics
    {
        public Statics()
        {
            try
            {
                //FR5.8
            }
            catch (LatitudeException e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }
    }
}