using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.orkut;

namespace CSharpBenchmark.presentation.gui.orkut
{
    //Functional requirement 3.1.2
    //Test case 67: Only class presentation.gui.orkut.ApplicationGUI may have a dependency with domain.orkut.Application 
    //Result: TRUE
    public class ApplicationGUI
    {
        public ApplicationGUI()
        {
            //FR5.1
            Console.WriteLine(Application.getName());
        }
    }
}