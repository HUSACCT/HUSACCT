using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.presentation.gui.observer.spotify;

namespace CSharpBenchmark.domain.spotify
{

    public class Song
    {
        private String name;
        private SpotifyGUI gui;

        public Song(SpotifyGUI gui)
        {
            this.gui = gui;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
            gui.updateGUI();
        }
    }
}