package infrastructure.database.derby;

import java.util.List;
//Functional requirement 2.5
//Test case 10: All interfaces in package infrastructure.database (including all subpackages and subclasses) must have prefix "I"
//Result: TRUE
interface ISavedTweets {
	public List<String> getSavedTweets(String country);
}
