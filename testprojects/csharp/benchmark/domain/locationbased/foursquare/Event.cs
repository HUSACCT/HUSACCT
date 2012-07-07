using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare;

namespace CSharpBenchmark.domain.locationbased.foursquare
{
    //Functional requirement 3.2
    //Test case 124: Class domain.locationbased.foursquare.Event is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.EventDAO 
    //Result: FALSE
    public class Event
    {
        //FR5.5	
        private EventDAO dao;

        public Event()
        {
            //FR5.2
            Console.WriteLine(dao.name);
        }
    }
}