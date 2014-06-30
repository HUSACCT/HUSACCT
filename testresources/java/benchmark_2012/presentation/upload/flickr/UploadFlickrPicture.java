package presentation.upload.flickr;

import java.util.List;

import presentation.annotations.Copyright;
import presentation.exception.CustomException;
import presentation.upload.Upload;
import domain.flickr.FlickrPicture;
import domain.flickr.Tag;
//Functional requirement 3.1
//Test case 34: Class presentation.upload.flickr.UploadFlickrPicture is allowed to use class
// - annotations.Copyright
// - presentation.upload.flickr.IFlickrUpload
// - presentation.upload.Upload
// - domain.flickr.FlickrPicture
// - domain.flickr.Tag
//Result: TRUE

//FR 5.3 + 5.4
public class UploadFlickrPicture extends Upload implements IFlickrUpload{
	//FR 5.6 Annotation of an attribute
	@Copyright	
	public UploadFlickrPicture(){
		try {
			//FR 5.1 invocation of a constructor
			FlickrPicture picture = new FlickrPicture();
			//FR 5.1
			upload(picture);
			//FR 5.8
		} catch (CustomException e) {
			e.printStackTrace();
		}
	}

	//Functional requirement 5.1 method argument
	public void upload(FlickrPicture content) throws CustomException {
		//FR 5.1
		List<Tag> tags = content.getTags();
		for(Tag tag : tags){
			System.out.println(tag.toString());
		}
		//FR5.8
		throw new CustomException("Error while printing file");
	}
}