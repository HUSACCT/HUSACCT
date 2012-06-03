package infrastructure.database.eloquera;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import infrastructure.database.Datasource;
//Functional requirement 2.6
//Test case 29: All classes in package infrastructure.database.eloquera must extend class infrastructure.database.DatabaseCollection
//Result: FALSE
public class EloqueraConnectionSettings extends Datasource {
	private static final String MYSQL_DB_CONN = "jdbc:mysql://145.89.21.30:8521:cursus01";
	private static final String MYSQL_DB_USER = "THO7_2010_4A_TEAM3";
	private static final String MYSQL_DB_PASS = "THO7_2010_4A_TEAM3";

	public Connection getConnection() {
		Connection connection = null;
		try {
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);
			connection = DriverManager.getConnection(MYSQL_DB_CONN, MYSQL_DB_USER, MYSQL_DB_PASS);
		}          
		catch (ClassNotFoundException e) {
			System.out.println("Couldnot find the database driver " + e.getMessage());
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return connection;
	}    
}
