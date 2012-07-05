using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.blog;

namespace CSharpBenchmark.domain.blog.wordpress
{

    //Functional requirement 3.3
    //Test case 170: Class domain.blog.wordpress.MyBlog is allowed to have dependencies on infrastructure.blog.*. Exception: Not on infrastructure.blog.ILocation.
    //Result: TRUE/FALSE

    //FR5.3
    public class MyBlog : Blog, ILocation
    {

    }
}
