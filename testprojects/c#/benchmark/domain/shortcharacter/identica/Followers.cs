using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.asocialmedia;

namespace CSharpBenchmark.domain.shortcharacter.identica
{
    //Functional requirement 3.1.1
    //Test case 43: Class domain.shortcharacter.identica.Followers may only have a dependency with class infrastructure.socialmedia.SocialNetworkInfo
    //Result: FALSE
    public class Followers
    {
        public Followers()
        {
            //FR5.2
            Console.WriteLine(ASocialNetworkInfo.message);
        }
    }
}