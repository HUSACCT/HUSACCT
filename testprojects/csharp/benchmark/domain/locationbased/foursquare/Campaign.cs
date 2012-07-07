using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare;

namespace CSharpBenchmark.domain.locationbased.foursquare
{
    //Functional requirement 3.2
    //Test case 120: Class domain.locationbased.foursquare.Campaign is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.CampaignDAO 
    //Result: FALSE
    public class Campaign
    {
        //FR5.5
        private CampaignDAO dao;

        public Campaign()
        {
            //FR5.1
            Console.WriteLine(dao.getCampaignType());
        }
    }
}