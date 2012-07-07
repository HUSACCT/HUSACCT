using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.facebook;

namespace CSharpBenchmark.presentation.gui.facebook
{

    //Functional requirement 3.1.2
    //Test case 74: Only class presentation.gui.facebook.MusicGUI may have a dependency with domain.facebook.Song
    //Result: FALSE
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