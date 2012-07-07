using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia;

namespace CSharpBenchmark.domain.shortcharacter.twitter
{
    //Functional requirement 3.1.1
    //Test case 52: Class domain.shortcharacter.twitter.Timeline may only have a dependency with class infrastructure.socialmedia.SocialNetwork 
    //Result: TRUE
    public class Timeline
    {
        //FR5.5
        public string getNetworkInformation(SocialNetworkInfo info)
        {
            return "";
        }
    }
}