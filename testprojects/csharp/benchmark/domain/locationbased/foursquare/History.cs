using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare;

namespace CSharpBenchmark.domain.locationbased.foursquare
{

    //Functional requirement 3.2
    //Test case 128: Class domain.locationbased.foursquare.History is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.HistoryDAO 
    //Result: FALSE

    //FR5.3
    public class History : HistoryDAO
    {

    }
}
