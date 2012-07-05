using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.domain.stumbleupon
{

    public class StumbleAnnotation : Attribute
    {
        public string stumbleId
        {
            get;
            set;
        }
    }
}
