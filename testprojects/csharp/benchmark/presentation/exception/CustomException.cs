using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.presentation.exception
{

    public class CustomException : Exception
    {
        public CustomException(String message)
            : base(message)
        {
        }
    }
}