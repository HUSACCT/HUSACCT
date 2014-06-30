package presentation.gui.stumbleupon;

import domain.stumbleupon.Song;
//Functional requirement 3.2.2
//Test case 165: All classes in package presentation.gui.stumbleupon are not allowed to use in a not direct lower layer
//Result: TRUE
public class MusicGUI{
	//FR5.5	
	private Song song;

	public MusicGUI(){
		//FR5.2
		System.out.println(song.artist);
	}
}