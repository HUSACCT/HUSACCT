using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.asocialmedia;

namespace CSharpBenchmark.domain.shortcharacter.identica
{
    //Functional requirement 3.1.1
    //Test case 37: Class domain.shortcharacter.identica.Account may only have a dependency with class infrastructure.asocialmedia.ASocialNetwork
    //Result: FALSE
    public class Account
    {
        public Account()
        {
            //FR5.1
            new ASocialNetwork();
        }
    }
}