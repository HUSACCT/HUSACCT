using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using FlickrNet;

namespace CSharpBenchmark.infrastructure.socialmedia.flickr
{

    //Functional requirement 2.3
    //Test case 6: Only class infrastructure.socialmedia.flickr.Backup is allowed to use the  FlickrNet.dll library file
    //Result: TRUE
    public class Backup
    {
        private String nsid = null;
        private Flickr flickr = null;
        private string Frob;
        private string AuthToken;

        public Backup(string apiKey, string sharedSecret)
        {
            flickr = new Flickr(apiKey, sharedSecret);

            Frob = flickr.AuthGetFrob();

            AuthToken = flickr.AuthCalcUrl(Frob, AuthLevel.Write);
        }

        public void GetPhoto()
        {
            Auth auth = flickr.AuthCheckToken(AuthToken);

            PhotoSearchOptions options = new PhotoSearchOptions();
            options.UserId = "1234567@N01"; // Your NSID
            options.PerPage = 100; // 100 is the default anyway
            FlickrNet.PhotoCollection photos = flickr.PhotosSearch(options);
            doBackup(photos);
        }

        public void doBackup(PhotoCollection photos)
        {

            foreach (var photo in photos)
            {
                flickr.PhotosSetMeta(photo.PhotoId, photo.Title, null);

                PhotosetCollection sets = flickr.PhotosetsGetList();
                var set = sets[0];

                flickr.PhotosetsAddPhoto(set.PhotosetId, set.Title);
            }
        }
    }
}