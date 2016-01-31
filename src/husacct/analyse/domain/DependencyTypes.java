package husacct.analyse.domain;

import java.util.EnumSet;

public enum DependencyTypes {

    ACCESS("Access"),
    CALL("Call"),
    DECLARATION("Declaration"),
    IMPORT("Import"),
    INHERITANCE("Inheritance"),
    REFERENCE("Reference");

    private final String dependencyType;

    private DependencyTypes(String dependencyType) {
        this.dependencyType = dependencyType;
    }

    @Override
    public String toString() {
        return dependencyType;
    }

    public static boolean isValidDependencyType(String dependencyType) {
        for (DependencyTypes option : EnumSet.allOf(DependencyTypes.class)) {
            if (option.toString().equals(dependencyType)) {
                return true;
            }
        }
        return false;
    }
}
