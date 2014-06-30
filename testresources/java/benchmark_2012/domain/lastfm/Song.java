package domain.lastfm;

import java.util.Date;

public class Song {
	private String name;
	private Date release;
	private Album album;
	private Artist artist;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Date getRelease() {
		return release;
	}

	public void setRelease(Date release) {
		this.release = release;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}
}