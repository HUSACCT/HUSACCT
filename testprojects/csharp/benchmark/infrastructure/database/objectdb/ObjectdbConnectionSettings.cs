using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data.SqlClient;

namespace CSharpBenchmark.infrastructure.database.objectdb
{
    //Functional requirement 2.6
    //Test case 28: All classes in package infrastructure.database.objectdb must extend class infrastructure.database.DatabaseCollection
    //Result: FALSE
    public abstract class ObjectdbConnectionSettings : DataCollection
    {
        public SqlConnection getConnection()
        {
            return null;
        }
    }
}