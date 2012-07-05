using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.latitude;

namespace CSharpBenchmark.domain.locationbased.latitude
{

    //Functional requirement 3.2
    //Test case 119: Class domain.locationbased.latitude.Campaign is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.CampaignDAO 
    //Result: TRUE
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