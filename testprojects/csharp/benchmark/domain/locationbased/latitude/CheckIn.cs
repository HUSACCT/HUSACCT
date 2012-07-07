using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.latitude;

namespace CSharpBenchmark.domain.locationbased.latitude
{

    //Functional requirement 3.2
    //Test case 121: Class domain.locationbased.latitude.CheckIn is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.CheckInDAO 
    //Result: TRUE
    public class CheckIn
    {
        public CheckIn()
        {
            //FR5.2
            Console.WriteLine(CheckInDAO.currentLocation);
        }
    }
}