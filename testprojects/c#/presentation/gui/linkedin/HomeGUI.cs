using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.presentation.exception;

namespace CSharpBenchmark.presentation.gui.linkedin
{
    //Functional requirement 4.4
    //Test case 198: Exceptions in class presentation.gui.linkedin.HomeGUI are only allowed when data is exchanged in datatype String
    //Result: TRUE
    public class HomeGUI
    {
        public HomeGUI()
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