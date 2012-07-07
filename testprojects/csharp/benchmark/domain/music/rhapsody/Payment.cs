using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.blog;

namespace CSharpBenchmark.domain.music.rhapsody
{
    //Functional requirement 3.3
    //Test case 175: Class domain.blog.wordpress.MyBlog is allowed to have a dependency with the class in infrastructure.blog except when infrastructure.blog.BlogAnnotation is used
    //Result: TRUE/FALSE

    [BlogAnnotation]
    public class Payment
    {
        //FR5.2
        private MyBlogStory mystory;
    }
}