using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia;

namespace CSharpBenchmark.domain.shortcharacter.twitter
{
    //Functional requirement 3.1.1
    //Test case 36: Class domain.shortcharacter.twitter.Account may only have a dependency with class infrastructure.socialmedia.SocialNetwork
    //Result: TRUE
    public class Account
    {
        public Account()
        {
            //FR5.1
            new SocialNetwork();
        }
    }
}