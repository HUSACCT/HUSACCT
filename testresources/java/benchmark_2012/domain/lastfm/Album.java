package domain.lastfm;

import java.util.Date;

public class Album {
	private String name;
	private Date releaseDate;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getReleaseDate() {
		return releaseDate;
	}
	
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
}