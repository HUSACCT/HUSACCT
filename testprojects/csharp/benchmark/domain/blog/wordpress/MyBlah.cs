using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.blog;

namespace CSharpBenchmark.domain.blog.wordpress
{
    //Functional requirement 3.3
    //Test case 173: Class domain.blog.wordpress.MyBlah must have dependencies on infrastructure.blog.*. Exception: Must not on infrastructure.blog.ILocation ( it does not count as a mandatory dependency).
    //Result: TRUE/FALSE

    //FR5.3
    public class MyBlah : ILocation
    {

    }
}
