using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.facebook;

namespace CSharpBenchmark.infrastructure.socialmedia.facebook.dao
{

    //Functional requirement 3.1.2
    //Test case 72: Only class presentation.gui.facebook.LinkGUI may have a dependency with domain.facebook.Link
    //Result: FALSE
    public class LinkDAO
    {
        public LinkDAO()
        {
            //FR5.2
            Console.WriteLine(Link.linkUrl);
        }
    }
}