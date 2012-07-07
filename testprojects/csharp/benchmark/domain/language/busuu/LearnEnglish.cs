using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia;

namespace CSharpBenchmark.domain.language.busuu
{

    //Functional requirement 3.1.3
    //Test case 96: Class domain.language.busuu.LearnEnglish must use class infrastructure.socialmedia.SocialNetworkInfo 
    //Result: TRUE
    public class LearnEnglish
    {
        public String sniName;
        public LearnEnglish()
        {
            //FR5.2
            //Console.WriteLine(SocialNetworkInfo.message);
        }
        public void testAccessStaticAttribute()
        {
            SocialNetworkInfo.message = "klant";
        }

        public void testAccessStaticFinalAttribute()
        {
            sniName = SocialNetworkInfo.name;
        }
    }
}