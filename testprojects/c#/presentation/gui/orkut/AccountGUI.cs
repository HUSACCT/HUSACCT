using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.orkut;

namespace CSharpBenchmark.presentation.gui.orkut
{
    //Functional requirement 3.1.2
    //Test case 65: Only class presentation.gui.orkut.AccountGUI may have a dependency with domain.orkut.Account 
    //Result: TRUE
    public class AccountGUI
    {
        public AccountGUI()
        {
            //FR5.1
            new Account();
        }
    }
}