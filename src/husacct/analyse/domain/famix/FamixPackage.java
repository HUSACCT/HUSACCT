package husacct.analyse.domain.famix;

class FamixPackage extends FamixEntity {

    public String belongsToPackage;

    @Override
    public boolean equals(Object object) {
        return object instanceof FamixPackage && super.uniqueName.equals(((FamixPackage) object).uniqueName);
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
