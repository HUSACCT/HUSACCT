using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.facebook;

namespace CSharpBenchmark.presentation.gui.facebook
{

    //Functional requirement 3.1.2
    //Test case 82: Only class presentation.gui.facebook.PrivacySettingsGUI may have a dependency with domain.facebook.PrivacySettings
    //Result: FALSE
    public class PrivacySettingsGUI
    {
        //FR5.5
        public String getPrivacySettings(PrivacySettings settings)
        {
            return "";
        }
    }
}