using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.asocialmedia;

namespace CSharpBenchmark.domain.shortcharacter.identica
{
    //Functional requirement 3.1.1
    //Test case 55: Class domain.shortcharacter.identica.PopularMessages may only have a dependency with class infrastructure.socialmedia.SocialTagAnnotion 
    //Result: FALSE

    //FR5.6
    [ASocialTagAnnotation(tags = new string[] { "three", "different", "tags" })]
    public class PopularMessages
    {

    }
}