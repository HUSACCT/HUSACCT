using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.blog;

namespace CSharpBenchmark.domain.blog.wordpress
{
    //Functional requirement 3.3
    //Test case 172: Class domain.blog.wordpress.MyStory is the only class allowed to depend on infrastructure.blog.*. Exception: domain.blog.wordpress.MyBlog is also allowed.
    //Result: TRUE/FALSE

    //FR5.3
    public class MyStory : Blog, ILocation
    {

    }
}
