package husacct.analyse.task.analyse.java.analysing;

import java.util.EnumSet;

enum SkippedJavaTypes {

    STRING("String"),
    INT("int"),
    BOOLEAN("boolean"),
    OBJECT("Object"),
    BYTE("byte"),
    CHAR("char"),
    CLASS("Class");
    private final String type;

    private SkippedJavaTypes(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static boolean isSkippable(String type) {
        for (SkippedJavaTypes skippedType : EnumSet.allOf(SkippedJavaTypes.class)) {
            if (skippedType.toString().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
