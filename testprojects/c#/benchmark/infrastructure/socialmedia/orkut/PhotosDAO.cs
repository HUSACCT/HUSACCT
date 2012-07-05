using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using OrkutAPILibrary;

namespace CSharpBenchmark.infrastructure.socialmedia.orkut
{
public class PhotosDAO {
	//Functional requirement 2.3
	//Test case 7: Only class infrastructure.socialmedia.orkut.PhotosDAO is allowed to use the OrkutAPILibrary.dll library file
	//Result: FALSE
	
	public static void say(String s) { Console.WriteLine(s); }
	public static void listPhotos(){
		var test = OrkutLibrary.Initialize(OrkutAPILibrary.Enums.ApplicationEnvironment.Desktop, tokenManager, null);

        var photos = test.GetPhotosFromAlbum("userid1", "album1", 0, 20);
        foreach(var photo in photos.List)
        {
            say("Photo ID        : " + photo.Id);
            say("Photo Title     : " + photo.PhotoTitle);
            say("Photo URL       : " + photo.PhotoUrl);
            say("Thumbnail URL   : " + photo.ThumbnailUrl);
            say("");
        }
	}

public static DotNetOpenAuth.OAuth.ChannelElements.IConsumerTokenManager tokenManager { get; set; }}
}