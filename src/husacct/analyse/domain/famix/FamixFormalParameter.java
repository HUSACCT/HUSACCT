package husacct.analyse.domain.famix;

import java.util.ArrayList;
import java.util.List;

class FamixFormalParameter extends FamixStructuralEntity{

	public String belongsToMethod;
	//extra attribute: declaredTypes
	//the return type of a parameter could simply be a String, int, double etc
	//but it can also be a list containing other declareTypes such as arrayLists and Hashmaps
	//In that case, all of the items from that list can be stored in declaredTypes
	//i.e. if this is a parameter: HashMap<User, HomeAddress> then the returntype is still a HashMap, 
	//but now the declaredTypes have 2 properties: a User and a HomeAddress object.
	public List<String> declaredTypes = new ArrayList<String>(); 
	//public int position;
	
}
