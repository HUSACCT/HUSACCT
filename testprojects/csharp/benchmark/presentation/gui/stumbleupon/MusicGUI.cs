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
    public class MusicGUI
    {
        //FR5.5	
        private Song song;

        public MusicGUI()
        {
            //FR5.2
            Console.WriteLine(song.artist);
        }
    }
}