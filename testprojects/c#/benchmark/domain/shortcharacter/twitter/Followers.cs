using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia;

namespace CSharpBenchmark.domain.shortcharacter.twitter
{
    //Functional requirement 3.1.1
    //Test case 40: Class domain.shortcharacter.twitter.Followers may only have a dependency with class infrastructure.socialmedia.SocialNetworkInfo 
    //Result: TRUE
    public class Followers
    {
        //FR5.5
        private SocialNetwork socialnetwork;

        public Followers()
        {
            //FR5.1
            Console.WriteLine(socialnetwork.getSocialNetworkType());
        }
    }
}