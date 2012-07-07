using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.domain.stumbleupon
{
    public class PrivacySettings
    {
        public static String[] settings = { "no privacy", "block unknown" };

        public static String[] getPrivacySettings()
        {
            String[] returnStringArray = { "no privacy", "block unknown" };
            return returnStringArray;
        }
    }
}