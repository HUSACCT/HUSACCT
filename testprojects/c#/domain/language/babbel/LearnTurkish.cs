using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.asocialmedia;

namespace CSharpBenchmark.domain.language.babbel
{

    //Functional requirement 3.1.3
    //Test case 111: Class domain.language.babbel.LearnTurkish must use class infrastructure.socialmedia.SocialMediaException 
    //Result: FALSE
    public class LearnTurkish
    {
        public LearnTurkish()
        {
            try
            {

                //FR5.8
            }
            catch (ASocialMediaException e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }
    }
}