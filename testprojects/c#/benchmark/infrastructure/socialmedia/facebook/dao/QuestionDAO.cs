using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.facebook;

namespace CSharpBenchmark.infrastructure.socialmedia.facebook.dao
{

    //Functional requirement 3.1.2
    //Test case 84: Only class presentation.gui.facebook.QuestionGUI may have a dependency with domain.facebook.FacebookAnnotation
    //Result: FALSE

    //FR5.6
    [FacebookAnnotation(facebookId = "1234567890")]
    public class QuestionDAO
    {
    }
}