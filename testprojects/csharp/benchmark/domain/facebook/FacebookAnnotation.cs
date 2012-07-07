using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.domain.facebook
{
    //Functional requirement 3.1.2
    //Test case 84: Only class presentation.gui.facebook.QuestionGUI may have a dependency with domain.facebook.FacebookAnnotation
    //Result: FALSE

    public class FacebookAnnotation : Attribute
    {
        public string facebookId
        {
            get;
            set;
        }
    }
}
