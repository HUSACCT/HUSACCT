using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.flickr;

namespace CSharpBenchmark.presentation.upload.flickr
{
    public interface IFlickrUpload
    {
        void upload(FlickrPicture content);
    }
}