using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.linkedin;

namespace CSharpBenchmark.presentation.exception
{

    public class LinkedInException : Exception
    {

        public LinkedInException(LinkedInDTO transferAccount)
            : base("name " + transferAccount.getName() + " password: " + transferAccount.getPassword())
        {
        }

        public LinkedInException(String message)
            : base(message)
        {
        }
    }
}