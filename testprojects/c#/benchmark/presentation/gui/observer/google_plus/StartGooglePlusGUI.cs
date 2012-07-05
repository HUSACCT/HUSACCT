using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.google_plus;

namespace CSharpBenchmark.presentation.gui.observer.google_plus
{

    public class StartGooglePlusGUI
    {
        public StartGooglePlusGUI()
        {
            GooglePlusGUI gui = new GooglePlusGUI();

            Circle circle = new Circle();
            circle.attach(gui);

            Contact c1 = new Contact("CHenry");
            c1.attach(gui);
            circle.addContact(c1);
            c1.setName("Henry");
        }
    }
}