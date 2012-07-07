using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.domain.orkut
{
    public class OrkutException : Exception
    {
        public OrkutException(string message)
            : base(message)
        {

        }
    }
}