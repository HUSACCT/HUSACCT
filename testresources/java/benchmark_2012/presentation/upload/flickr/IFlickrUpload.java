package presentation.upload.flickr;

import presentation.exception.CustomException;
import domain.flickr.FlickrPicture;

public interface IFlickrUpload {
	void upload(FlickrPicture content) throws CustomException;
}