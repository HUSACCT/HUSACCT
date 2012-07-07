using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.socialmedia
{

    public class SocialMediaException : Exception
    {
        public SocialMediaException(String message)
            : base(message)
        {
        }
    }
}