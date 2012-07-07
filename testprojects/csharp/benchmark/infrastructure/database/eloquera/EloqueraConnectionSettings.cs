using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data.SqlClient;
using System.Data.SQLite;

namespace CSharpBenchmark.infrastructure.database.eloquera
{
    //Functional requirement 2.6
    //Test case 29: All classes in package infrastructure.database.eloquera must extend class infrastructure.database.DatabaseCollection
    //Result: FALSE
    public class EloqueraConnectionSettings : DataCollection
    {

        public SQLiteConnection getConnection()
        {
            SQLiteConnection connection = new SQLiteConnection(@"Server=(local)\sqlexpress;Integrated Security=True;" +
                  "Database=DB");
            return connection;
        }
    }
}
