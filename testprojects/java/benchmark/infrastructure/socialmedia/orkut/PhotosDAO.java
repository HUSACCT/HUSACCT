package infrastructure.socialmedia.orkut;

import com.google.orkut.client.api.BatchTransaction;
import com.google.orkut.client.api.GetPhotosTx;
import com.google.orkut.client.api.OrkutAdapter;
import com.google.orkut.client.api.Photo;
import com.google.orkut.client.api.UploadPhotoTx;

public class PhotosDAO {
	//Functional requirement 2.3
	//Test case 7: Only class infrastructure.socialmedia.orkut.PhotosDAO is allowed to use the orkut.jar library file
	//Result: FALSE
	
	public static void say(String s) { System.err.println(s); }

	public static void listPhotos(OrkutAdapter orkad) throws Exception {
		say("What's the album ID?");
		String albumId = readline();

		GetPhotosTx tx = orkad.getPhotosTF().getSelfPhotos(albumId);
		BatchTransaction btx = orkad.newBatch();
		tx.setCount(20); // get up to 20 photos
		btx.add(tx);
		say("Getting photos...");
		orkad.submitBatch(btx);

		if (tx.hasError()) {
			say("*** Error getting photos:" + tx.getError());
			return;
		}

		int i;
		say(String.valueOf(tx.getPhotoCount()) + " photos returned.");
		for (i = 0; i < tx.getPhotoCount(); i++) {
			Photo p = tx.getPhoto(i);
			say("Photo ID        : " + p.getId());
			say("Photo Title     : " + p.getTitle());
			say("Photo URL       : " + p.getUrl());
			say("Thumbnail URL   : " + p.getThumbnailUrl());
			say("");
		}
	}

	public static void uploadPhoto(OrkutAdapter orkad) throws Exception {
		say("What's the album ID?");
		String albumId = readline();
		say("What's the path to the JPG file?");
		String filePath = readline();
		say("What's the title of the photo?");
		String title = readline();

		UploadPhotoTx tx = orkad.getPhotosTF().uploadPhoto(albumId,filePath,title);
		BatchTransaction btx = orkad.newBatch();
		btx.add(tx);
		say("Submitting photo...");
		orkad.submitBatch(btx);

		if (tx.hasError()) {
			say("*** Error uploading photo:" + tx.getError());
		}
		else say("Success.");
	}

	public static String readline() throws Exception {
		java.io.BufferedReader br = new java.io.BufferedReader(
				new java.io.InputStreamReader(
						System.in));
		return br.readLine();
	}
}