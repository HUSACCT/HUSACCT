using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.facebook;

namespace CSharpBenchmark.infrastructure.socialmedia.facebook.dao
{

    //Functional requirement 3.1.2
    //Test case 88: Only class presentation.gui.facebook.WallGUI may have a dependency with domain.facebook.FacebookException
    //Result: FALSE
    public class WallDAO
    {
        public WallDAO()
        {
            try
            {
                //FR5.8
            }
            catch (FacebookException e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

    }
}