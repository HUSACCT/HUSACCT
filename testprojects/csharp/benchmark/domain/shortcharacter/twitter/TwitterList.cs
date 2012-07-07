using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia;

namespace CSharpBenchmark.domain.shortcharacter.twitter
{

    //Functional requirement 3.1.1
    //Test case 58: Class domain.shortcharacter.twitter.TwitterList may only have a dependency with class infrastructure.socialmedia.SocialMediaException 
    //Result: TRUE

    public class TwitterList
    {
        public TwitterList()
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