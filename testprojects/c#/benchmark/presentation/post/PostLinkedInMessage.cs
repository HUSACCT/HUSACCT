using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia;

namespace CSharpBenchmark.presentation.post
{
    //Functional requirement 3.2.2
    //Test case 166: The classes in package presentation.post are not allowed to use modules in a not direct lower layer
    //Result: FALSE
    public class PostLinkedInMessage
    {
        //FR5.5
        private SocialNetwork socialnetwork;

        public PostLinkedInMessage()
        {
            //FR5.2
            Console.WriteLine(socialnetwork.type);
        }
    }
}