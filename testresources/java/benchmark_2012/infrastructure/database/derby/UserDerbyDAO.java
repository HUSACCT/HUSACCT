package infrastructure.database.derby;

import java.util.Collections;
import java.util.List;
//Functional requirement 2.3
//Test case 3: Only the classes in package infrastructure.socialmedia (including all subpackages and subclasses) are allowed to use the JDBC library
//Result: FALSE
public class UserDerbyDAO extends DerbyConnectionSettings {
	public List<String> getUsers(){
		query("SELECT USERNAME FROM USERS");
		return Collections.emptyList();
	}
}