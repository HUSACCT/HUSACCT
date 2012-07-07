using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia;

namespace CSharpBenchmark.domain.language.busuu
{

    //Functional requirement 3.1.3
    //Test case 92: Class domain.language.busuu.LearnChinese must use class infrastructure.socialmedia.SocialNetworkInfo 
    //Result: TRUE
    public class LearnChinese
    {
        public LearnChinese()
        {
            //FR5.1
            Console.WriteLine(SocialNetworkInfo.getInfo());
        }
    }
}