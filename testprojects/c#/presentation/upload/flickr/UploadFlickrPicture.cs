using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.presentation.exception;
using CSharpBenchmark.presentation.annotations;
using CSharpBenchmark.domain.flickr;

namespace CSharpBenchmark.presentation.upload.flickr
{
    //Functional requirement 3.1
    //Test case 34: Class presentation.upload.flickr.UploadFlickrPicture is allowed to use class
    // - annotations.Copyright
    // - presentation.upload.flickr.IFlickrUpload
    // - presentation.upload.Upload
    // - domain.flickr.FlickrPicture
    // - domain.flickr.Tag
    //Result: TRUE

    //FR 5.3 + 5.4
    public class UploadFlickrPicture : Upload, IFlickrUpload
    {
        //FR 5.6 Annotation of an attribute
        [Copyright]
        public UploadFlickrPicture()
        {
            try
            {
                //FR 5.1 invocation of a constructor
                FlickrPicture picture = new FlickrPicture();
                //FR 5.1
                upload(picture);
                //FR 5.8
            }
            catch (CustomException e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

        //Functional requirement 5.1 method argument
        public void upload(FlickrPicture content)
        {
            //FR 5.1
            List<Tag> tags = content.getTags();
            foreach (Tag tag in tags)
            {
                Console.WriteLine(tag.ToString());
            }
            //FR5.8
            throw new CustomException("Error while printing file");
        }
    }
}