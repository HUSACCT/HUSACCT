package husacct.analyse.domain.famix;

import husacct.analyse.abstraction.dto.LibraryDTO;

public class FamixLibrary extends FamixDecompositionEntity {

    public boolean isPackage = true;
	public String physicalPath;

    public boolean equals(FamixLibrary other) {
        return ((other.belongsToPackage == this.belongsToPackage && other.uniqueName == this.uniqueName)); 
    }

    public LibraryDTO getDTO() {
    	LibraryDTO dto = new LibraryDTO(name, uniqueName, visibility, external, belongsToPackage, isPackage, physicalPath);
    	return dto;
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