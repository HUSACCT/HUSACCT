using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.domain.gowalla
{
    public class GoWallaException : Exception
    {
        public GoWallaException(string message)
            : base(message)
        {
        }
    }
}