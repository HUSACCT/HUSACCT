using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.locationbased.foursquare;

namespace CSharpBenchmark.domain.locationbased.foursquare
{
    //Functional requirement 3.2
    //Test case 142: Class domain.locationbased.foursuare.User is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.UserDAO
    //Result: FALSE
    public class User
    {
        public String sniName;
        public String sniMessage;
        private UserDAO userdao;

        public User()
        {
            //FR5.2
            //System.out.println(UserDAO.name);
        }

        public void testAccessFinalAttribute()
        {
            sniMessage = userdao.message;
        }

        public void testAccessStaticFinalAttribute()
        {
            sniName = UserDAO.name;
        }
    }
}