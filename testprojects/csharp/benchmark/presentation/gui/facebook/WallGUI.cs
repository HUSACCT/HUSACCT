using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.facebook;

namespace CSharpBenchmark.presentation.gui.facebook
{

    //Functional requirement 3.1.2
    //Test case 88: Only class presentation.gui.facebook.WallGUI may have a dependency with domain.facebook.FacebookException
    //Result: FALSE
    public class WallGUI
    {
        public WallGUI()
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