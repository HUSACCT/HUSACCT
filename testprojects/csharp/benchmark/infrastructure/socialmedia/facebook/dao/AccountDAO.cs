using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.facebook;

namespace CSharpBenchmark.infrastructure.socialmedia.facebook.dao
{
    //Functional requirement 3.1.2
    //Test case 66: Only class presentation.gui.facebook.AccountGUI may have a dependency with class domain.Facebook.Account
    //Result: FALSE
    public class AccountDAO
    {
        public AccountDAO()
        {
            //FR5.1
            new Account();
        }
    }
}