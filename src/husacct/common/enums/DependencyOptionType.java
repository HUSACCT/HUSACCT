package husacct.common.enums;

/**
 * Created by Lars on 31-5-2016.
 */
public enum DependencyOptionType {
    ALL_DEPENDENCY("AllDependencies"),
    ONLY_UMLLINKS("UmlLinks"),
    ACCESS_CALL_REFERENCE("AccessCallReferenceDependencies");

    private String type;

    DependencyOptionType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static DependencyOptionType getOptionType(String s) {
        return null;
    }
}
