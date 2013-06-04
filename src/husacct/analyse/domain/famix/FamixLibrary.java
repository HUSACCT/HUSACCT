package husacct.analyse.domain.famix;

public class FamixLibrary extends FamixEntity {

    public String belongsToPackage;

    public boolean equals(FamixLibrary other) {
        return ((other.belongsToPackage == this.belongsToPackage && other.uniqueName == this.uniqueName)); 
    }

    @Override
    public String toString() {
        String libraryRepresentation = "";
        libraryRepresentation += "\nUnique Name: " + super.uniqueName;
        libraryRepresentation += "\nBelongs to Library: " + belongsToPackage;
        libraryRepresentation += "\nName: " + super.name + "\n\n";
        return libraryRepresentation;
    }
}