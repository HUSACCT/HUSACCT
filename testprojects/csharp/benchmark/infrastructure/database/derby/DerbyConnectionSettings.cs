using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data.SQLite;

namespace CSharpBenchmark.infrastructure.database.derby
{
    //Functional requirement 2.3
    //Test case 3: Only the classes in package infrastructure.socialmedia (including all subpackages and subclasses) are allowed to use the SQLite library
    //Result: FALSE
    public abstract class DerbyConnectionSettings : Datasource
    {
        private static readonly String CONN_STRING = "Data Source=DemoT.db;Version=3;New=False;Compress=True";
        SQLiteConnection connection = new SQLiteConnection(CONN_STRING);

        public override SQLiteConnection getConnection()
        {
            connection.Open();
            return connection;
        }
    }
}