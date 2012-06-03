package infrastructure.socialmedia.pinterest;

import java.util.List;

public class BoardDTO {
	private String title;
	private List<String> pins;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getPins() {
		return pins;
	}

	public void setPins(List<String> pins) {
		this.pins = pins;
	}
}