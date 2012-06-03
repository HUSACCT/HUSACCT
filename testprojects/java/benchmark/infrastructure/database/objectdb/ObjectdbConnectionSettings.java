package infrastructure.database.objectdb;

import infrastructure.database.DataCollection;

import java.sql.Connection;
//Functional requirement 2.6
//Test case 28: All classes in package infrastructure.database.objectdb must extend class infrastructure.database.DatabaseCollection
//Result: FALSE
public abstract class ObjectdbConnectionSettings extends DataCollection {
	public Connection getConnection() {
		return null;
	}
}