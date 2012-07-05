using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia;

namespace CSharpBenchmark.domain.shortcharacter.twitter
{

    //Functional requirement 3.1.1
    //Test case 44: Class domain.shortcharacter.twitter.Mentions may only have a dependency with class infrastructure.socialmedia.SocialNetwork
    //Result: TRUE
    public class Mentions
    {
        //FR5.5
        private SocialNetwork socialnetwork;

        public Mentions()
        {
            //FR5.2
            Console.WriteLine(socialnetwork.type);
        }
    }
}