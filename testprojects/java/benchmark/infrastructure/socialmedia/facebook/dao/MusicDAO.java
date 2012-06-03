package infrastructure.socialmedia.facebook.dao;

import domain.facebook.Song;

//Functional requirement 3.1.2
//Test case 74: Only class presentation.gui.facebook.MusicGUI may have a dependency with domain.facebook.Song
//Result: FALSE
public class MusicDAO{
	//FR5.5	
	private Song song;

	public MusicDAO(){
		//FR5.2
		System.out.println(song.artist);
	}
}