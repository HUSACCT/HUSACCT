using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data.SqlClient;
using System.Data.SQLite;

namespace CSharpBenchmark.infrastructure.database.mysql
{
    //Functional requirement 2.7
    //Test case 32: All classes in package infrastructure.database.postgresql must implement interface infrastructure.database.mysql.IMySQL
    //Result: FALSE
    public class MySqlConnectionSettings : Datasource, IMySql
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
