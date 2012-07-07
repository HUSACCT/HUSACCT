using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.facebook;

namespace CSharpBenchmark.presentation.gui.facebook
{

    //Functional requirement 3.1.2
    //Test case 72: Only class presentation.gui.facebook.LinkGUI may have a dependency with domain.facebook.Link
    //Result: FALSE
    public class LinkGUI
    {
        public LinkGUI()
        {
            //FR5.2
            Console.WriteLine(Link.linkUrl);
        }
    }
}