package domain.flickr;

import java.util.List;

public class FlickrPicture extends Flickr {
	//Access of a property or field
	private List<Tag> tags;

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
}