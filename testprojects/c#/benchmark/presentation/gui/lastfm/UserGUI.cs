using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.lastfm;

namespace CSharpBenchmark.presentation.gui.lastfm
{
    //Functional requirement 3.3 + 3.1.3
    //Test case 90: Package presentation.gui.lastfm must have a dependency with domain.lastfm except class presentation.gui.lastfm.RadioGUI
    //Result: TRUE

    //Functional requirement 3.3 + 3.1.1
    //Test case 91: Package presentation.gui.lastfm is only allowed to use package domain.lastfm, except class presentation.gui.lastfm.RadioGUI (is only allowed to use domain.twitter.PrivateTweet)
    //Result: TRUE

    //Functional requirement 3.3 + 3.1.2
    //Test case 92: Package presentation.gui.lastfm must have a dependency with domain.lastfm except class presentation.gui.lastfm.RadioGUI (is only allowed to use domain.twitter)
    //Result: TRUE
    public class UserGUI
    {
        private List<Song> recentlyListened;

        public UserGUI()
        {
            loadGUI();
        }

        private void loadGUI()
        {
            loadRecentlyListened();
        }

        private void loadRecentlyListened()
        {
            foreach (Song s in getRecentlyListened())
            {
                Artist a = s.getArtist();
                Console.WriteLine(a.getName());
                Console.WriteLine(" - ");
                Console.WriteLine(s.getArtist().getName());
            }
        }

        public List<Song> getRecentlyListened()
        {
            return recentlyListened;
        }

        public void setRecentlyListened(List<Song> recentlyListened)
        {
            this.recentlyListened = recentlyListened;
        }
    }
}