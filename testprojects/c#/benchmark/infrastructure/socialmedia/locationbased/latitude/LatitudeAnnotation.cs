using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.socialmedia.locationbased.latitude
{
    //Functional requirement 3.2
    //Test case 133: Class domain.locationbased.latitude.Settings is not allowed to use annotation infrastructure.socialmedia.locationbased.foursquare.FourSquareAnnotation
    //Result: TRUE
    public class LatitudeAnnotation : Attribute
    {
        public string Author
        {
            get;
            private set;
        }

        public LatitudeAnnotation(string author = "Themaopdracht 7 tester")
        {
            Author = author;
        }
    }
}