using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare;

namespace CSharpBenchmark.domain.locationbased.foursquare
{
    //Functional requirement 3.2
    //Test case 144: Class domain.locationbased.foursquare.Venue is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.VenueDAO
    //Result: FALSE
    public class Venue
    {
        //FR5.5
        public VenueDAO getVenues()
        {
            return null;
        }
    }
}