package infrastructure.database.mssql;

import infrastructure.database.DataCollection;

import java.sql.Connection;
//Functional requirement 2.6
//Test case 24: Class infrastructure.database.mssql.UserMsSqlDAO may not extend abstract class infrastructure.database.DataCollection
//Result: FALSE
public abstract class MsSqlConnectionSettings extends DataCollection {
	public Connection getConnection() {
		return null;
	}
}