package husacct.analyse.domain.famix;

public class FamixInterface extends FamixEntity {

    public String belongsToPackage;

    public boolean equals(FamixInterface other) {
        return ((other.belongsToPackage == this.belongsToPackage && other.uniqueName == this.uniqueName)); 
    }

    public String toString() {
        String s = "Interface\n ";
        s += "UniqueName: " + uniqueName;
        s += "\nName: " + name;
        s += "\nBelongs to package; " + belongsToPackage;
        s += "\n\n";
        return s;
    }
}
