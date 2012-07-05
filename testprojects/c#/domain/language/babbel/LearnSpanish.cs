using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.asocialmedia;

namespace CSharpBenchmark.domain.language.babbel
{

    //Functional requirement 3.1.3
    //Test case 107: Class domain.language.babbel.LearnSpanish must use class infrastructure.socialmedia.SocialTagAnnotion 
    //Result: FALSE

    //FR5.6
    [ASocialTagAnnotation(tags = new string[] { "three", "different", "tags" })]
    public class LearnSpanish
    {

    }
}