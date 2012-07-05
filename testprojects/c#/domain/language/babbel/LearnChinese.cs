using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.asocialmedia;

namespace CSharpBenchmark.domain.language.babbel
{

    //Functional requirement 3.1.3
    //Test case 93: Class domain.language.babbel.LearnChinese must use class infrastructure.socialmedia.SocialNetworkInfo 
    //Result: FALSE
    public class LearnChinese
    {
        public LearnChinese()
        {
            //FR5.1
            Console.WriteLine(ASocialNetworkInfo.getInfo());
        }
    }
}