using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.latitude;

namespace CSharpBenchmark.domain.locationbased.latitude
{

    //Functional requirement 3.2
    //Test case 139: Class domain.locationbased.latitude.Tip is not allowed to use enumeration infrastructure.socialmedia.locationbased.foursqaure.TipDAO
    //Result: TRUE
    public class Tip
    {
        public Tip()
        {
            //FR5.2
            Console.WriteLine(TipDAO.ONE);
        }
    }
}