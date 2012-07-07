using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.asocialmedia;

namespace CSharpBenchmark.domain.shortcharacter.identica
{

    //Functional requirement 3.1.1
    //Test case 39: Class domain.shortcharacter.identica.FeaturedMessages may only have a dependency with class infrastructure.socialmedia.SocialNetworkInfo
    //Result: FALSE
    public class FeaturedMessages
    {
        public FeaturedMessages()
        {
            //FR5.1
            Console.WriteLine(ASocialNetworkInfo.getInfo());
        }
    }
}