package husacct.analyse.domain.famix;

public class FamixLibrary extends FamixDecompositionEntity {

    public String physicalPath;

    public boolean equals(FamixLibrary other) {
        return ((other.belongsToPackage == this.belongsToPackage && other.uniqueName == this.uniqueName)); 
    }

    @Override
    public String toString() {
        String libraryRepresentation = "";
        libraryRepresentation += "\nUnique Name: " + super.uniqueName;
        libraryRepresentation += "\nBelongs to Library: " + belongsToPackage;
        libraryRepresentation += "\nPhysical Path: " + physicalPath;
        libraryRepresentation += "\nName: " + super.name + "\n\n";
        return libraryRepresentation;
    }
}