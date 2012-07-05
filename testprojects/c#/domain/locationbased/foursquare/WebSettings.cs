using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Igloo.SharpSquare.Core;

namespace CSharpBenchmark.domain.locationbased.foursquare
{
    //Functional requirement 3.2
    //Test case 146: Class domain.locationbased.foursquare,WebSettings is not allowed to use library SharpSquare.dll
    //Result: FALSE
    public class WebSettings
    {
        public void handleCallback()
        {
            try
            {
                string clientId = "CLIENT_ID";
                string clientSecret = "CLIEND_SECRET";
                string redirectUri = "REDIRECT_URI";
                SharpSquare sharpSquare = new SharpSquare(clientId, clientSecret);
                sharpSquare.GetAuthenticateUrl(redirectUri);

            }
            catch (Exception  e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }
    }
}