using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare;

namespace CSharpBenchmark.domain.locationbased.foursquare
{
    //Functional requirement 3.2
    //Test case 122: Class domain.locationbased.foursquare.CheckIn is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.CheckInDAO 
    //Result: FALSE
    public class CheckIn
    {
        public CheckIn()
        {
            //FR5.2
            Console.WriteLine(CheckInDAO.currentLocation);
        }
    }
}