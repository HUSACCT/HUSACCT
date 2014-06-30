package domain.spotify;

import presentation.gui.observer.spotify.SpotifyGUI;

public class Song {
	private String name;
	private SpotifyGUI gui;
	
	public Song(SpotifyGUI gui){
		this.gui = gui;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		gui.updateGUI();		
	}	
}