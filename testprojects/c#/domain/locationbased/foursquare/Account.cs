using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare;

namespace CSharpBenchmark.domain.locationbased.foursquare
{
    //Functional requirement 3.2
    //Test case 116: Class domain.locationbased.foursquare.Account is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.AccountDAO 
    //Result: FALSE
    public class Account
    {
        public Account()
        {
            //FR5.1
            new AccountDAO();
        }
    }
}