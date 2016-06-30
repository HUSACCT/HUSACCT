package husacct.common.enums;

/**
 * Created by Lars on 31-5-2016.
 */
public enum DependencyTypeOption {
    ALL_DEPENDENCY("AllDependencies"),
    ACCESS_CALL_REFERENCE("AccessCallReferenceDependencies"),
    ONLY_UMLLINKS("UmlLinks");

    private String type;

    DependencyTypeOption(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static DependencyTypeOption getOptionType(String s) {
        return null;
    }
}
