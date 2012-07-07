using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.domain.facebook
{
    //Functional requirement 3.1.2
    //Test case 82: Only class presentation.gui.facebook.PrivacySettingsGUI may have a dependency with domain.facebook.PrivacySettings
    //Result: FALSE
    public class PrivacySettings
    {
        public static string[] settings = { "no privacy", "block unknown" };

        public static string[] getPrivacySettings()
        {
            string[] returnStringArray = { "no privacy", "block unknown" };
            return returnStringArray;
        }
    }
}
