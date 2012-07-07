using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data.SQLite;
using System.Data;

namespace CSharpBenchmark.infrastructure.database
{
    //Functional requirement 2.3
    //Test case 3: Only the classes in package infrastructure.socialmedia (including all subpackages and subclasses) are allowed to use the SQLite library
    //Result: FALSE
    public abstract class Datasource
    {
        public abstract SQLiteConnection getConnection();
        private DataSet sqlDataSet = new DataSet();
        private DataTable sqlDataTable = new DataTable();

        protected DataTable query(String query)
        {
            getConnection().Open();
            SQLiteCommand sqlCommand = getConnection().CreateCommand();
            string CommandText = query;
            SQLiteDataAdapter DB = new SQLiteDataAdapter(CommandText, getConnection());
            sqlDataSet.Reset();
            DB.Fill(sqlDataSet);
            sqlDataTable = sqlDataSet.Tables[0];
            getConnection().Close();

            return sqlDataTable;
        }
    }
}