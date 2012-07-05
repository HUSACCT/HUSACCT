using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json.Linq;

namespace CSharpBenchmark.infrastructure.socialmedia.facebook.api
{
    //Functional requirement 2.3
    //Test case 4: Only the classes in package infrastructure.socialmedia.facebook.api are allowed to use the Newtonsoft.Json.dll library file
    //Result: TRUE

    //Functional requirement 4.5
    //Test case 202: Class infrastructure.socialmedia.facebook.API.FacebookGraph is only allowed to get data from the FacebookGraph REST API.
    //Result: TRUE
    class FacebookGraph
    {
        public bool isValidFacebookGraphUrl(string url) {
		//Valid URL: https://graph.facebook.com/19292868552
		if(url.Contains("https://graph.facebook.com/")){			
			try{
				//JObject is from the Newtonsoft.Json.dll library
				JObject json = readJsonFromUrl(url);
				if(json.ToString().Equals("false")){
					return false;
				}
				else{
					return true;
				}	
			}
			catch(Exception e){
                Console.WriteLine(e.StackTrace);
			}
		}
		else{
			return false;
		}
		return false;
	}

        //JObject is from the Newtonsoft.Json.dll library
        private JObject readJsonFromUrl(string url)
        {
            JObject jObject = null;
            using (var webClient = new System.Net.WebClient())
            {
                try
                {
                    var json = webClient.DownloadString(url);
                    jObject = JObject.Parse(json);
                    return jObject;
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.StackTrace);
                }
            }
            return jObject;
        }
    }
}