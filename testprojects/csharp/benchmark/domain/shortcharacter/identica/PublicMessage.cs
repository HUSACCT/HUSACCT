using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.asocialmedia;

namespace CSharpBenchmark.domain.shortcharacter.identica
{

    //Functional requirement 3.1.1
    //Test case 59: Class domain.shortcharacter.identica.PublicMessage may only have a dependency with class infrastructure.socialmedia.SocialMediaException 
    //Result: FALSE
    public class PublicMessage
    {
        public PublicMessage()
        {
            try
            {

                //FR5.8
            }
            catch (ASocialMediaException e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }
    }
}