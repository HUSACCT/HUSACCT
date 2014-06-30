package presentation.upload.pinterest;

import presentation.gui.pinterest.PinterestGUI;
//Functional requirement 4.3
//Test case 97: The class in package presentation.gui.pinterest.PinterestController is not allowed to use the classes in the package presentation.upload.pinterest 
//				except when data is exchanged if necessary in Data Transfer Object 
//				*	presentation.gui.pinterest.PinterestController calls infrastructure.socialmedia.pinterest.PinDAO.countpinsOnGUI(PinterestGUI gui)
//Result: FALSE
public class UploadPinterestPicture {
	public void Post(PinterestGUI gui){

	}
}