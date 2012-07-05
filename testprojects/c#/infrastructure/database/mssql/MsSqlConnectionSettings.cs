using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data.SqlClient;

namespace CSharpBenchmark.infrastructure.database.myssql
{
    //Functional requirement 2.6
    //Test case 24: Class infrastructure.database.mssql.UserMsSqlDAO may not extend abstract class infrastructure.database.DataCollection
    //Result: FALSE
    public abstract class MsSqlConnectionSettings : DataCollection
    {
        public SqlConnection getConnection()
        {
            return null;
        }
    }
}