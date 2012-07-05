using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.presentation.exception;

namespace CSharpBenchmark.presentation.gui.linkedin
{
    //Functional requirement 4.4
    //Test case 201: Exceptions in class presentation.gui.linkedin.ContactGUI are only allowed when data is exchanged in Data Transfer Objects
    //Result: FALSE
    public class ContactGUI
    {
        public ContactGUI()
        {
            try
            {
                throw new LinkedInException("Error in homeGUI");
            }
            catch (LinkedInException e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }
    }
}