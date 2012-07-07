using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.socialmedia.locationbased.latitude
{

    //Functional requirement 3.2
    //Test case 119: Class domain.locationbased.latitude.Campaign is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.CampaignDAO 
    //Result: TRUE
    public class CampaignDAO
    {
        public String getCampaignType()
        {
            return "commercial";
        }
    }
}