using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.asocialmedia;

namespace CSharpBenchmark.domain.language.babbel
{
    //Functional requirement 3.1.3
    //Test case 91: Class domain.language.babbel.LearnArabic must use infrastructure.socialmedia.SocialNetwork 
    //Result: FALSE
    public class LearnArabic
    {
        public LearnArabic()
        {
            //FR5.1
            new ASocialNetwork();
        }
    }
}