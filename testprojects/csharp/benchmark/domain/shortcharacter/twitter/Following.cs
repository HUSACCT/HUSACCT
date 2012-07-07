using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia;

namespace CSharpBenchmark.domain.shortcharacter.twitter
{
    //Functional requirement 3.1.1
    //Test case 42: Class domain.shortcharacter.twitter.Following may only have a dependency with class infrastructure.socialmedia.SocialNetworkInfo
    //Result: TRUE
    public class Following
    {
        public Following()
        {
            //FR5.2
            Console.WriteLine(SocialNetworkInfo.message);
        }
    }
}