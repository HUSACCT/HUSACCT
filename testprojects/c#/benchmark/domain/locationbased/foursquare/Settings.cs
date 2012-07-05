using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare;

namespace CSharpBenchmark.domain.locationbased.foursquare
{

    //Functional requirement 3.2
    //Test case 134: Class domain.locationbased.foursquare.Settings is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.FourSquareAnnotation
    //Result: FALSE

    //FR5.6
    [FourSquareAnnotation]
    public class Settings
    {

    }
}