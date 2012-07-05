using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare;

namespace CSharpBenchmark.domain.locationbased.foursquare
{
    //Functional requirement 3.2
    //Test case 118: Class domain.locationbased.foursquare.Badges is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.BadgesDAO 
    //Result: FALSE
    public class Badges
    {
        public Badges()
        {
            //FR5.1
            Console.WriteLine(BadgesDAO.getAllBadges());
        }
    }
}