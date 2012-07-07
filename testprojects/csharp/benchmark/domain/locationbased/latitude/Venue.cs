using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.latitude;

namespace CSharpBenchmark.domain.locationbased.latitude
{


    //Functional requirement 3.2
    //Test case 143: Class domain.locationbased.latitude.Venue is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.VenueDAO
    //Result: TRUE
    public class Venue
    {
        //FR5.5
        public VenueDAO getVenues()
        {
            return null;
        }
    }
}