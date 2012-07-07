using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.orkut;

namespace CSharpBenchmark.presentation.gui.orkut
{

    //Functional requirement 3.1.2
    //Test case 73: Only class presentation.gui.orkut.GTalkGUI may have a dependency with domain.orkut.GTalk
    //Result: TRUE
    public class GTalkGUI
    {
        //FR5.5
        private GTalk gtalk;

        public GTalkGUI()
        {
            //FR5.2
            Console.WriteLine(gtalk.status);
        }
    }
}