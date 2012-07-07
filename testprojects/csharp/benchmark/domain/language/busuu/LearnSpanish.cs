using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia;

namespace CSharpBenchmark.domain.language.busuu
{

    //Functional requirement 3.1.3
    //Test case 108: Class domain.language.busuu.LearnSpanish must use class infrastructure.socialmedia.SocialTagAnnotion 
    //Result: TRUE

    //FR5.6

    [SocialTagAnnotation(tags = new string[] { "three", "different", "tags" })]
    public class LearnSpanish
    {

    }
}