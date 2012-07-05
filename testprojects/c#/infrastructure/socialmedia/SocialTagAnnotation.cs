using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.socialmedia
{
    public class SocialTagAnnotation : Attribute
    {
        public string[] tags
        {
            set;
            get;
        }
    }
}