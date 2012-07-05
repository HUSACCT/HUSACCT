using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.domain.facebook
{
    public class FacebookException : Exception
    {
        //Functional requirement 3.1.2
        //Test case 88: Only class presentation.gui.facebook.WallGUI may have a dependency with domain.facebook.FacebookException
        //Result: FALSE
        public FacebookException(string message) : base (message)
        {
 
        }
    }
}
