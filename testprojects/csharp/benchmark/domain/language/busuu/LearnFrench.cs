using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia;

namespace CSharpBenchmark.domain.language.busuu
{

    //Functional requirement 3.1.3
    //Test case 98: Class domain.language.busuu.LearnFrench must use class infrastructure.socialmedia.SocialNetwork 
    //Result: TRUE
    public class LearnFrench
    {
        //FR5.5
        private SocialNetwork socialnetwork;

        public LearnFrench()
        {
            //FR5.2
            //Console.WriteLine(socialnetwork.type);
            socialnetwork.type = "klant";
        }
    }
}