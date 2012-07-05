using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.domain.stumbleupon
{
    public class StumbleException : Exception
    {
        public StumbleException(string message)
            : base(message)
        {
        }
    }
}