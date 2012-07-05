using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.stumbleupon;

namespace CSharpBenchmark.presentation.gui.stumbleupon
{
    //Functional requirement 3.2.2
    //Test case 165: All classes in package presentation.gui.stumbleupon are not allowed to use in a not direct lower layer
    //Result: TRUE
    public class PrivacySettingsGUI
    {
        //FR5.5
        public String getPrivacySettings(PrivacySettings settings)
        {
            return "";
        }
    }
}