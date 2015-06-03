package husacct.analyse.domain.famix;

import husacct.analyse.abstraction.dto.PackageDTO;

class FamixPackage extends FamixDecompositionEntity {

    @Override
    public boolean equals(Object object) {
        return object instanceof FamixPackage && super.uniqueName.equals(((FamixPackage) object).uniqueName);
    }

    public PackageDTO getDTO() {
    	PackageDTO dto = new PackageDTO(name, uniqueName, visibility, external, belongsToPackage);
    	return dto;
    }
    
    @Override
    public String toString() {
        String packageRepresentation = "";
        packageRepresentation += "\nUnique Name: " + super.uniqueName;
        packageRepresentation += "\nBelongs to Package: " + belongsToPackage;
        packageRepresentation += "\nName: " + super.name + "\n\n";
        return packageRepresentation;
    }
}
