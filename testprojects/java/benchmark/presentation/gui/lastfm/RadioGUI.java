package presentation.gui.lastfm;

import javax.swing.JFrame;

import domain.shortcharacter.twitter.PrivateTweet;

//Functional requirement 3.3 + 3.1.1
//Test case 91: Package presentation.gui.lastfm is only allowed to use package domain.lastfm, except class presentation.gui.lastfm.RadioGUI (is only allowed to use domain.twitter.PrivateTweet)
//Result: TRUE

//Functional requirement 3.3 + 3.1.2
//Test case 92: Package presentation.gui.lastfm must have a dependency with domain.lastfm except class presentation.gui.lastfm.RadioGUI (is only allowed to use domain.twitter)
//Result: TRUE
public class RadioGUI extends JFrame {
	public void postPrivateTweet(){
		new PrivateTweet();
	}
}