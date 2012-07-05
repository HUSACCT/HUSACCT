using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare
{
    //Functional requirement 3.2
    //Test case 134: Class domain.locationbased.foursquare.Settings is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.FourSquareAnnotation
    //Result: FALSE

    public class FourSquareAnnotation : Attribute
    {
        public string Author
        {
            get;
            set;
        }

        public FourSquareAnnotation(string author = "Themaopdracht 7 tester")
        {
            Author = author;
        }
    }
}