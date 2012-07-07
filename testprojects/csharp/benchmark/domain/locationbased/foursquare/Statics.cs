using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare;

namespace CSharpBenchmark.domain.locationbased.foursquare
{

    //Functional requirement 3.2
    //Test case 138: Class domain.locationbased.foursquare.Statics is not allowed to use exception infrastructure.socialmedia.locationbased.foursquare.FourSqaureException 
    //Result: FALSE
    public class Statics
    {
        public Statics()
        {
            try
            {
                //FR5.8
            }
            catch (FourSquareException e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

    }
}