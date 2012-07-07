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
    public class MusicGUI
    {
        private List<Song> popular;
        private List<Song> ambient;
        private List<Song> pop;

        public MusicGUI()
        {
            loadGUI();
        }

        private void loadGUI()
        {
            loadPopularList();
            loadAmbientList();
            loadPop();
        }

        private void loadPopularList()
        {
            foreach (Song s in getPopular())
            {
                Artist a = s.getArtist();
                Console.WriteLine(a.getName());
                Console.WriteLine(" - ");
                Console.WriteLine(s.getArtist().getName());
            }
        }

        private void loadAmbientList(){
		foreach(Song s in getAmbient()){
			Artist a = s.getArtist();			
			Console.WriteLine((a.getName()));
			Console.WriteLine(" - ");
			Console.WriteLine(s.getArtist().getName());
		}
	}

        private void loadPop()
        {
            foreach (Song s in getAmbient())
            {
                Artist a = s.getArtist();
                Console.WriteLine(a.getName());
                Console.WriteLine(" - ");
                Console.WriteLine(s.getArtist().getName());
            }
        }

        public List<Song> getPopular()
        {
            return popular;
        }

        public void setPopular(List<Song> popular)
        {
            this.popular = popular;
        }

        public List<Song> getAmbient()
        {
            return ambient;
        }

        public void setAmbient(List<Song> ambient)
        {
            this.ambient = ambient;
        }

        public List<Song> getPop()
        {
            return pop;
        }

        public void setPop(List<Song> pop)
        {
            this.pop = pop;
        }
    }
}