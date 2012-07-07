using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia;
using CSharpBenchmark.infrastructure.asocialmedia;

namespace CSharpBenchmark.domain.shortcharacter.twitter
{

    //Functional requirement 3.1.1
    //Test case 60: Class domain.shortcharacter.twitter.Websettings may only have a dependency with SocialNetworkInfo
    //Result: TRUE
    public class WebSettings
    {
        public WebSettings()
        {
            char[] chararray = SocialNetworkInfo.getInfo().ToCharArray();
            List<string> stringList = new List<string>();
            foreach (char c in chararray)
            {
                stringList.Add(ASocialNetworkInfo.getInfo());
            }
        }
    }
}