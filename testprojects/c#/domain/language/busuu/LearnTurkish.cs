using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia;

namespace CSharpBenchmark.domain.language.busuu
{

    //Functional requirement 3.1.3
    //Test case 112: Class domain.language.busuu.LearnTurkish must use class infrastructure.socialmedia.SocialMediaException 
    //Result: TRUE
    public class LearnTurkish
    {
        public LearnTurkish()
        {
            try
            {
                //FR5.8
            }
            catch (SocialMediaException e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }
    }
}