using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.orkut;

namespace CSharpBenchmark.presentation.gui.orkut
{

    //Functional requirement 3.1.2
    //Test case 81: Only class presentation.gui.orkut.ScrapsGUI may have a dependency with domain.orkut.Scraps
    //Result: TRUE
    public class ScrapsGUI
    {
        //FR5.5
        public String getScraps(Scraps[] scraps)
        {
            return "";
        }
    }
}