using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.socialmedia.netlog
{
    //Functional requirement 3.2.1
    //Test case 160: All classes in package infrastructure.socialmedia.netlog are not allowed to use modules in a higher layer 
    //Result: TRUE
    public class AdvertiseDAO
    {
        public AdvertiseDAO()
        {
            //FR5.1
            Console.WriteLine(SocialNetworkInfo.getInfo());
        }
    }
}