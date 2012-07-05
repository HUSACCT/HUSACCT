using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.asocialmedia;

namespace CSharpBenchmark.domain.shortcharacter.identica
{

    //Functional requirement 3.1.1
    //Test case 53: Class domain.shortcharacter.identica.Message may only have a dependency with class infrastructure.asocialmedia.ASocialNetwork
    //Result: FALSE

    public class Message
    {
        //FR5.5
        public string getNetworkInformation(ASocialNetworkInfo info)
        {
            return "";
        }
    }
}