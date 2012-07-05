using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.facebook;

namespace CSharpBenchmark.infrastructure.socialmedia.facebook.dao
{

    //Functional requirement 3.1.2
    //Test case 70: Only class presentation.gui.facebook.EventGUI may have a dependency with domain.facebook.Event
    //Result: FALSE
    public class EventDAO
    {
        //FR5.5
        private Event ev;

        public EventDAO()
        {
            //FR5.1
            Console.WriteLine(ev.getEventType());
        }
    }
}