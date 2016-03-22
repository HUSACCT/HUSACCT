package husacct.analyse.task.analyser.java;

import java.util.EnumSet;

enum SkippedTypes {

    STRING("String"),
    INT("int"),
    BOOLEAN("boolean"),
    OBJECT("Object"),
    BYTE("byte"),
    CHAR("char"),
    CLASS("Class");
    private final String type;

    private SkippedTypes(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static boolean isSkippable(String type) {
        for (SkippedTypes skippedType : EnumSet.allOf(SkippedTypes.class)) {
            if (skippedType.toString().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
