using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.asocialmedia;

namespace CSharpBenchmark.domain.shortcharacter.identica
{

    //Functional requirement 3.1.1
    //Test case 41: Class domain.shortcharacter.twitter.Followers may only have a dependency with class infrastructure.socialmedia.SocialNetworkInfo 
    //Result: FALSE
    public class Feeds
    {
        //FR5.5
        private ASocialNetwork asocialnetwork;

        //FR5.1
        public Feeds()
        {
            Console.WriteLine(asocialnetwork.getASocialNetworkType());
        }
    }
}