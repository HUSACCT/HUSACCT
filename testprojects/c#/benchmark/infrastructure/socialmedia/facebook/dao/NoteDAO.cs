using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.facebook;

namespace CSharpBenchmark.infrastructure.socialmedia.facebook.dao
{
    //Functional requirement 3.1.2
    //Test case 76: Only class presentation.gui.facebook.NoteGUI may have a dependency with domain.facebook.Menubar
    //Result: FALSE

    //FR5.3
    public class NoteDAO : Menubar
    {

    }
}