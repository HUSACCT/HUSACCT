package infrastructure.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
//Functional requirement 2.3
//Test case 3: Only the classes in package infrastructure.socialmedia (including all subpackages and subclasses) are allowed to use the JDBC library
//Result: FALSE
public abstract class Datasource {
	public abstract Connection getConnection();

	protected ResultSet query(String query){
		try {
			return getConnection().prepareStatement(query/*+ result_cache */).executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}	
}