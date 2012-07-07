using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;

namespace CSharpBenchmark.infrastructure.database.derby
{
    //Functional requirement 2.3
    //Test case 3: Only the classes in package infrastructure.socialmedia (including all subpackages and subclasses) are allowed to use the SQLite library
    //Result: FALSE
    public class UserDerbyDAO : DerbyConnectionSettings
    {
        public List<String> getUsers()
        {
            List<string> users = new List<string>();
            DataTable output =  query("SELECT USERNAME FROM USERS");
            foreach (var user in output.Columns)
            {
                users.Add(user.ToString());
            }
            return users;
        }
    }
}