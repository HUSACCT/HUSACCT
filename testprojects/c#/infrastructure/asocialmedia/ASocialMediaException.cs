using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.asocialmedia
{

    public class ASocialMediaException : Exception
    {
        public ASocialMediaException(string message)
            : base(message)
        {
        }
    }
}