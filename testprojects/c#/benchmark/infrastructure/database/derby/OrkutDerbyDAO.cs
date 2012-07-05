using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using OrkutAPILibrary;

namespace CSharpBenchmark.infrastructure.database.derby
{
    //Functional requirement 2.3
    //Test case 7: Only class infrastructure.socialmedia.orkut.PhotosDAO is allowed to use the OrkutAPILibrary.dll library file
    //Result: FALSE
    public class OrkutDerbyDAO
    {
        public static void say(String s) { Console.WriteLine(s); }
        public static DotNetOpenAuth.OAuth.ChannelElements.IConsumerTokenManager tokenManager { get; set; }
        public static void listActivities(string orkad)
        {
            var test = OrkutLibrary.Initialize(OrkutAPILibrary.Enums.ApplicationEnvironment.Desktop, tokenManager, null);
            var activities = test.GetActivitiesOfUser("10", 0, 10);
            foreach (var activity in activities.List)
            {
                say("Activity ID        : " + activity.ActivityId);
                say("Posted time        : " + activity.PostedTime);
                say("User id            : " + activity.UserId);
                say("Activity params    : " + activity.ActivityParameters);
                say("");
            }
        }
    }
}