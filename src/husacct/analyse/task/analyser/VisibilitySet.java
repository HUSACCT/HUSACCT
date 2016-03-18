package husacct.analyse.task.analyser;

import java.util.EnumSet;

public enum VisibilitySet {

    PUBLIC("public"),
    PRIVATE("private"),
    DEFAULT("default"),
    PROTECTED("protected");
    private final String visibillity;

    private VisibilitySet(String visibillity) {
        this.visibillity = visibillity;
    }

    @Override
    public String toString() {
        return visibillity;
    }

    public static boolean isValidVisibillity(String visibillty) {
        for (VisibilitySet option : EnumSet.allOf(VisibilitySet.class)) {
            if (option.toString().equals(visibillty)) {
                return true;
            }
        }
        return false;
    }
}
