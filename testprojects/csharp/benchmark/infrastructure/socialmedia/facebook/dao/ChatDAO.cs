using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.facebook;

namespace CSharpBenchmark.infrastructure.socialmedia.facebook.dao
{
    //Functional requirement 3.1.2
    //Test case 68: Only class presentation.gui.facebook.ChatGUI may have a dependency with domain.facebook.Message 
    //Result: FALSE
    public class ChatDAO
    {
        public ChatDAO()
        {
            //FR5.1
            Console.WriteLine(Message.getMessage());
        }
    }
}