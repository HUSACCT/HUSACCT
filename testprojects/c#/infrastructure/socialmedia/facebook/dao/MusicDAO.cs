using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.facebook;

namespace CSharpBenchmark.infrastructure.socialmedia.facebook.dao
{
    //Functional requirement 3.1.2
    //Test case 74: Only class presentation.gui.facebook.MusicGUI may have a dependency with domain.facebook.Song
    //Result: FALSE
    public class MusicDAO
    {
        //FR5.5	
        private Song song;

        public MusicDAO()
        {
            //FR5.2
            Console.WriteLine(song.artist);
        }
    }
}