package husacct.analyse.domain.famix;

public class FamixLibrary extends FamixEntity {

    public String belongsToPackage;

    public boolean equals(FamixLibrary other) {
        boolean result = true;
        result = result && (other.belongsToPackage == this.belongsToPackage);
        result = result && (other.uniqueName == this.uniqueName);
        return result;
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
