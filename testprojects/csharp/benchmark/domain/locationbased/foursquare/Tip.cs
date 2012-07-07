using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare;

namespace CSharpBenchmark.domain.locationbased.foursquare
{
    //Functional requirement 3.2
    //Test case 140: Class domain.locationbased.foursquare.Tip is not allowed to use enumeration infrastructure.socialmedia.locationbased.foursquare.TipDAO
    //Result: FALSE
    public class Tip
    {
        public Tip()
        {
            //FR5.2
            Console.WriteLine(TipDAO.ONE);
        }
    }
}