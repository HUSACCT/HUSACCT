using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.latitude;

namespace CSharpBenchmark.domain.locationbased.latitude
{

    //Functional requirement 3.2
    //Test case 115: Class domain.locationbased.latitude.Account is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.AccountDAO 
    //Result: TRUE
    public class Account
    {
        public Account()
        {
            //FR5.1
            new AccountDAO();
        }
    }
}