using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.latitude;

namespace CSharpBenchmark.domain.locationbased.latitude
{


    //Functional requirement 3.2
    //Test case 133: Class domain.locationbased.latitude.Settings is not allowed to use annotation infrastructure.socialmedia.locationbased.foursquare.FourSquareAnnotation
    //Result: TRUE

    //FR5.6
    [LatitudeAnnotation]
    public class Settings
    {

    }
}