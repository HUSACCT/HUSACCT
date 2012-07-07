using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.asocialmedia;

namespace CSharpBenchmark.domain.language.babbel
{
    //Functional requirement 3.1.3
    //Test case 95: Class domain.language.babbel.LearnDutch must use class infrastructure.socialmedia.SocialNetwork 
    //Result: FALSE
    public class LearnDutch
    {
        //FR5.5
        private ASocialNetwork asocialnetwork;

        //FR5.1
        public LearnDutch()
        {
            Console.WriteLine(asocialnetwork.getASocialNetworkType());
        }
    }
}