using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia;

namespace CSharpBenchmark.domain.language.busuu
{
    //Functional requirement 3.1.3
    //Test case 90: Class domain.language.busuu.LearnArabic must use class infrastructure.socialmedia.SocialNetwork
    //Result: TRUE
    public class LearnArabic
    {
        public LearnArabic()
        {
            //FR5.1
            new SocialNetwork();
        }
    }
}