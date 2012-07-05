using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia;

namespace CSharpBenchmark.domain.shortcharacter.twitter
{

    //Functional requirement 3.1.1
    //Test case 54: Class domain.shortcharacter.twitter.Trends may only have a dependency with class infrastructure.socialmedia.SocialTagAnnotion 
    //Result: TRUE

    //FR5.6
    [SocialTagAnnotation(tags = new string[] { "three", "different", "tags" })]
    public class Trends
    {

    }
}