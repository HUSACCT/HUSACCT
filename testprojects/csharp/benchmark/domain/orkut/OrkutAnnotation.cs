using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.domain.orkut
{
    public class OrkutAnnotation : Attribute
    {
        public string orkitId
        {
            get;
            private set;
        }

        public OrkutAnnotation(string orkit = "0987654321")
        {
            orkitId = orkit;
        }
    }
}