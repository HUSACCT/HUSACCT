using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.domain.facebook
{
    //Functional requirement 3.1.2
    //Test case 72: Only class presentation.gui.facebook.LinkGUI may have a dependency with domain.facebook.Link
    //Result: FALSE
    public class Link
    {
        public static string linkUrl = "http://www.facebook.com";
    }
}
