using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.blog;

namespace CSharpBenchmark.domain.music.rhapsody
{

    //Functional requirement 3.3
    //Test case 174: Class domain.music.rhapsody.Account is not allowed to depend on infrastructure.blog.*. Exception: It is allowed to depend on infrastructure.blog.MyItem.

    //Result: TRUE/FALSE

    //FR5.3
    public class Account : MyItem
    {

    }
}