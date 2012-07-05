using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia;

namespace CSharpBenchmark.domain.language.busuu
{

    //Functional requirement 3.1.3
    //Test case 94: Class domain.language.busuu.LearnDutch must use class infrastructure.socialmedia.SocialNetwork
    //Result: TRUE
    public class LearnDutch
    {
        //FR5.5
        private SocialNetwork socialnetwork;

        public LearnDutch()
        {
            //FR5.1
            Console.WriteLine(socialnetwork.getSocialNetworkType());
        }
    }
}