using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.linkedin;
using CSharpBenchmark.presentation.exception;

namespace CSharpBenchmark.presentation.gui.linkedin
{

    //Functional requirement 4.4
    //Test case 199: Exceptions in class presentation.gui.linkedin.ProfileGUI are only allowed when data is exchanged in datatype String
    //Result: FALSE
    public class ProfileGUI
    {
        public ProfileGUI()
        {
            LinkedInDTO transferAccount = new LinkedInDTO();
            transferAccount.setName("Mathew");
            transferAccount.setPassword("Schilke");

            try
            {
                throw new LinkedInException(transferAccount);
            }
            catch (LinkedInException e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }
    }
}