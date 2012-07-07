using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.orkut;

namespace CSharpBenchmark.presentation.gui.orkut
{
    //Functional requirement 3.1.2
    //Test case 71: Only class presentation.gui.orkut.CommunityGUI may have a dependency with domain.orkut.Community
    //Result: TRUE
    public class CommunityGUI
    {
        public CommunityGUI()
        {
            //FR5.2
            Console.WriteLine(Community.communityName);
        }
    }
}