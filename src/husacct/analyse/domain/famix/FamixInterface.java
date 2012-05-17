package husacct.analyse.domain.famix;

public class FamixInterface extends FamixEntity {
	
	public String belongsToPackage;
	
	public boolean equals(FamixInterface other){
		boolean result = true;
		result = result && (other.belongsToPackage == this.belongsToPackage);
		result = result && (other.uniqueName == this.uniqueName);
		return result;
	}
	
	public String toString(){
		String s = "Interface\n ";
		s += "UniqueName: " + uniqueName;
		s += "\nName: " + name;
		s += "\nBelongs to package; " + belongsToPackage;
		s += "\n\n";
		return s;
	}
	
}
