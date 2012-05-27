package infrastructure.database.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import infrastructure.database.Datasource;
//Functional requirement 2.3
//Test case 3: Only the classes in package infrastructure.socialmedia (including all subpackages and subclasses) are allowed to use the JDBC library
//Result: FALSE
public abstract class DerbyConnectionSettings extends Datasource{
	private static final String DERBY_DB_CONN = "jdbc:derby://localhost:1527/myDB;create=true;";
	private static final String DERBY_DB_USER = "me";
	private static final String DERBY_DB_PASS = "password";
	
	public Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(DERBY_DB_CONN, DERBY_DB_USER, DERBY_DB_PASS);
		} catch (SQLException ex) {
			System.out.println(ex);
		}
		return connection;
	}
}