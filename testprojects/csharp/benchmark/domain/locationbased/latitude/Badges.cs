using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.latitude;

namespace CSharpBenchmark.domain.locationbased.latitude
{

    //Functional requirement 3.2
    //Test case 117: Class domain.locationbased.latitude.Badges is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.BadgesDAO 
    //Result: TRUE
    public class Badges
    {
        public Badges()
        {
            //FR5.1
            Console.WriteLine(BadgesDAO.getAllBadges());
        }
    }
}