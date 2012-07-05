using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.socialmedia.facebook.api
{
    public class FacebookFacade
    {
        private FacebookGraph facebookGraph;

        public FacebookFacade()
        {
            facebookGraph = new FacebookGraph();
        }

        public bool getGraphId(String url)
        {
            try
            {
                return facebookGraph.isValidFacebookGraphUrl(url);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
            return false;
        }
    }
}