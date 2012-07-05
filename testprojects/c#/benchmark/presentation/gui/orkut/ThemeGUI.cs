using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.orkut;

namespace CSharpBenchmark.presentation.gui.orkut
{

    //Functional requirement 3.1.2
    //Test case 87: Only class presentation.gui.orkut.ThemeGUI may have a dependency with domain.orkut.OrkutException
    //Result: TRUE
    public class ThemeGUI
    {
        public ThemeGUI()
        {
            try
            {
                //FR5.8
            }
            catch (OrkutException e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }
    }
}