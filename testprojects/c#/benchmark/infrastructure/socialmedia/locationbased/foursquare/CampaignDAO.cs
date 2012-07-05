using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare
{
    //Functional requirement 3.2
    //Test case 120: Class domain.locationbased.foursquare.Campaign is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.CampaignDAO 
    //Result: FALSE
    public class CampaignDAO
    {
        public string getCampaignType()
        {
            return "commercial";
        }
    }
}