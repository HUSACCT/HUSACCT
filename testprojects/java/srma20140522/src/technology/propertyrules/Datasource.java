package technology.propertyrules;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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