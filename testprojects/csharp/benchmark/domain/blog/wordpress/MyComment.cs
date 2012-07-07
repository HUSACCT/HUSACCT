using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.blog;

namespace CSharpBenchmark.domain.blog.wordpress
{
    //Functional requirement 3.3
    //Test case 171: Class domain.blog.wordpress.MyComment is only allowed to have dependencies on infrastructure.blog.*. Exception: Not on infrastructure.blog.ILocation.

    //Result: TRUE/FALSE

    //FR5.4
    public class MyComment : Blog, ILocation {

    }
}
