package presentation.gui.lastfm;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import domain.lastfm.Artist;
import domain.lastfm.Song;
//Functional requirement 3.3 + 3.1.3
//Test case 90: Package presentation.gui.lastfm must have a dependency with domain.lastfm except class presentation.gui.lastfm.RadioGUI
//Result: TRUE

//Functional requirement 3.3 + 3.1.1
//Test case 91: Package presentation.gui.lastfm is only allowed to use package domain.lastfm, except class presentation.gui.lastfm.RadioGUI (is only allowed to use domain.twitter.PrivateTweet)
//Result: TRUE

//Functional requirement 3.3 + 3.1.2
//Test case 92: Package presentation.gui.lastfm must have a dependency with domain.lastfm except class presentation.gui.lastfm.RadioGUI (is only allowed to use domain.twitter)
//Result: TRUE
public class UserGUI extends JFrame {
	private List<Song> recentlyListened;

	public UserGUI(){
		loadGUI();
	}

	private void loadGUI(){
		this.setSize(250, 250);
		this.setLocation(300,200);
		this.getContentPane().add(BorderLayout.CENTER, new JTextArea(10, 40));
		loadRecentlyListened();
		this.setVisible(true);
	}

	private void loadRecentlyListened(){
		for(Song s : getRecentlyListened()){			
			Artist a = s.getArtist();			
			System.out.print(a.getName());
			System.out.print(" - ");
			System.out.println(s.getArtist().getName());
		}	
	}

	public List<Song> getRecentlyListened() {
		return recentlyListened;
	}

	public void setRecentlyListened(List<Song> recentlyListened) {
		this.recentlyListened = recentlyListened;
	}
}